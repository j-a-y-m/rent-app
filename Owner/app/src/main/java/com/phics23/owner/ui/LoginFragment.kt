package com.phics23.owner.ui

import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.security.ProviderInstaller
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.phics23.owner.R
import com.phics23.owner.data.repository.AuthRepository
import com.phics23.owner.databinding.FragmentLoginBinding
import com.phics23.owner.ui.viewModels.AuthViewModel
import com.phics23.owner.util.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {
    @Inject lateinit var auth : FirebaseAuth
    lateinit var binding : FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        viewModel.auth()
        viewModel.AuthResult.observe(viewLifecycleOwner){ result->
            when(result) {
                is Result.Success -> {
                    requireActivity().finish()
                    binding.root.findNavController().navigate(LoginFragmentDirections.actionFragmentLoginToMainActivity())
                }

            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.buttonSignIn.setOnClickListener {
                viewModel.login(binding.loginEmail.editText!!.text.toString(),binding.loginPassword.editText!!.text.toString())

        }



        viewModel.loginResult.observe(viewLifecycleOwner){  loginResult->
            Log.e("observe", "onViewCreated: "+loginResult )

            when(loginResult)
            {
                is Result.Success->
                {
                    requireActivity().finish()
                    val action = LoginFragmentDirections.actionFragmentLoginToMainActivity()
                    binding.root.findNavController().navigate(action)
                }
                is Result.Failure -> {
                    Snackbar.make(binding.mainContentView, loginResult.message, Snackbar.LENGTH_LONG).show()
                    Log.e("TAG", "onViewCreated: "+loginResult.message )
                }
                is Result.Loading ->
                {
                    binding.loginLoadingIndicator.visibility= View.VISIBLE
                    binding.mainContentView.visibility= View.GONE
                }
            }

        }


        binding.buttonForgotPassword.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                viewModel.forgotPassword(binding.loginEmail.toString())
            }
        }

        viewModel.ResetRequestResult.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    binding.loginLoadingIndicator.visibility= View.GONE
                    binding.mainContentView.visibility= View.VISIBLE
                    Snackbar.make(binding.mainContentView, result.data, Snackbar.LENGTH_LONG).show()
                }
                is Result.Failure -> {
                    binding.loginLoadingIndicator.visibility= View.GONE
                    binding.mainContentView.visibility= View.VISIBLE
                    Snackbar.make(binding.mainContentView, result.message, Snackbar.LENGTH_LONG).show()

                }
                is Result.Loading -> {
                    binding.loginLoadingIndicator.visibility= View.VISIBLE
                    binding.mainContentView.visibility= View.GONE
                }

            }

        }
        binding.buttonSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionFragmentLoginToFragmentSignup()
            binding.root.findNavController().navigate(action)

        }




    }


}