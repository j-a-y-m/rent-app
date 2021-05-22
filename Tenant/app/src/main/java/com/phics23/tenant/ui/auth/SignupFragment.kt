package com.phics23.tenant.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.security.ProviderInstaller
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.phics23.tenant.R

import com.phics23.tenant.ui.viewModels.AuthViewModel
import com.phics23.tenant.ui.viewModels.SignupViewModel
import com.phics23.tenant.util.Result
import com.phics23.tenant.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {

    lateinit var binding : FragmentSignupBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ProviderInstaller.installIfNeeded(requireContext())
        binding.buttonSignUp.setOnClickListener {
//                FirebaseFirestore.getInstance().collection("Owners").document("bc").set(hashMapOf("mc" to "firebase")).addOnCompleteListener {
//
//                    Log.e("TAG", "onViewCreated: fbfb" )
//                }
                binding.signupLoading.visibility= View.VISIBLE
                viewModel.signUp(binding.signupEmail.editText!!.text.toString(),
                    binding.signupPassword.editText!!.text.toString(),
                    binding.signupName.editText!!.text.toString(),
                    binding.signupAddress.editText!!.text.toString(),
                    binding.signupPhoneNumber.editText!!.text.toString()
                )


            viewModel.signupResult.observe(viewLifecycleOwner){ result->

                when(result)
                {
                    is Result.Success -> {
                        Snackbar.make(binding.signUpScreen, result.data, Snackbar.LENGTH_LONG).show()
                        val action = SignupFragmentDirections.actionFragmentSignupToFragmentLogin()
                        binding.root.findNavController().navigate(action)
                    }
                    is Result.Failure -> {
                        Snackbar.make(binding.signUpScreen, result.message, Snackbar.LENGTH_LONG).show()

                    }
                    is Result.Loading -> {
                        binding.signupLoading.visibility= View.VISIBLE
                        binding.signUpScreen.visibility = View.GONE
                    }
                }



            }


        }










    }

}