package com.phics23.owner.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.phics23.owner.R
import com.phics23.owner.databinding.FragmentAccountBinding
import com.phics23.owner.ui.viewModels.ProfileViewModel
import com.phics23.owner.util.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource

@AndroidEntryPoint
class AccountFragment : Fragment() {
    lateinit var binding : FragmentAccountBinding
    lateinit var easyImage : EasyImage
    val viewModel : ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater,container,false)
        viewModel.getOwnerProfile()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.ownerProfile.observe(viewLifecycleOwner) { result->

            when(result)
            {
                is Result.Success -> {
                    binding.ownerProfile = result.data
                }
                is Result.Failure -> createSnackBar(result.message)
            }
        }

        binding.shapeableImageView.setOnClickListener{
                easyImage = EasyImage.Builder(this.requireContext()).setChooserType(ChooserType.CAMERA_AND_GALLERY).allowMultiple(false).build()
                easyImage.openGallery(this)
        }

        binding.buttonLogout.setOnClickListener {

            viewModel.logOut()
            requireActivity().finish()
            val action  = AccountFragmentDirections.actionAccountFragmentToAuthActivity()
            findNavController().navigate(action)

        }

        binding.buttonResetPassword.setOnClickListener {
            viewModel.resetPassword()

        }
        viewModel.passwordResetRequestResult.observe(viewLifecycleOwner)
        {result->
            when (result) {
                is Result.Success -> {
                    createSnackBar(result.data)
                }
                is Result.Failure -> {
                    createSnackBar(result.message)
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, this.requireActivity(), object : EasyImage.Callbacks {
            val TAG = "easyimage"
            override fun onCanceled(source: MediaSource) {

                Log.d(TAG, "onCanceled: " + source)
            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                Log.d(TAG, "onImagePickerError: " )
            }

            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {

                viewModel.setProfileImage(imageFiles.get(0).file)
                Log.d(TAG, "onMediaFilesPicked: "+imageFiles.size)
            }
        })
    }


    fun createSnackBar( message: String)
    {
        Snackbar.make(binding.root,message, Snackbar.LENGTH_LONG).show()
    }


}