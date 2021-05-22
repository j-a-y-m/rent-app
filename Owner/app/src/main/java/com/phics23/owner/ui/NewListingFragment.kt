package com.phics23.owner.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.phics23.owner.databinding.FragmentNewlistingBinding
import com.phics23.owner.ui.adapters.UploadImagesAdapter
import com.phics23.owner.ui.viewModels.NewListingViewModel
import com.phics23.owner.util.Result
import dagger.hilt.android.AndroidEntryPoint
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource

//http function for payment that edits payment stuff , periodic function that calculates dues of users

@AndroidEntryPoint
class   NewListingFragment : Fragment() {
    private lateinit var binding : FragmentNewlistingBinding
    lateinit var easyImage : EasyImage
    lateinit var uploadImagesAdapter : UploadImagesAdapter
    val viewModel : NewListingViewModel by viewModels()

    private val TAG = "NewListingFragment"
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
            binding = FragmentNewlistingBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        easyImage = EasyImage.Builder(this.requireContext()).setChooserType(ChooserType.CAMERA_AND_GALLERY).allowMultiple(true).build()
        uploadImagesAdapter = UploadImagesAdapter(this,easyImage)
        binding.uploadImagesViewPagerNewlistingFragment.adapter = uploadImagesAdapter

//        observe vm.validform go to next fragment
//        vm has strings two way binded on publish/post click vm.validate if okay update validform else ba.error

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonSubmitNewListing.setOnClickListener {
            val title = binding.textInputTitle.text.toString()
            val address = binding.textInputAddress.text.toString()
            val city = binding.textInputTcity.text.toString()
            val area = binding.textInputArea.text.toString()
            val price = binding.textInputPrice.text.toString()
            val totalOccupants = binding.textInputOccupants.text.toString()
            val description = binding.textInputDescription.text.toString()
            val facilitiesChipGroup = binding.chipGroupFacilities.checkedChipIds
            val facilities = mutableListOf<String>()
            for (facilityId in facilitiesChipGroup)
            {
                if (facilityId!==View.NO_ID)
                {
                    facilities.add( binding.root.findViewById<Chip>(facilityId).text.toString())
                }

            }
            val listingTypeId = binding.chipGroupListingType.checkedChipId
            val listingType = if(listingTypeId==NO_ID) "" else binding.root.findViewById<Chip>(listingTypeId).text.toString()
            val listingBhkId = binding.chipGroupListingBhk.checkedChipId
            val listingBhk = if(listingBhkId==NO_ID) "" else binding.root.findViewById<Chip>(listingBhkId).text.toString()
            val imageFiles = uploadImagesAdapter.imageFileList
            try {
                viewModel.createNewListing(title,
                        address,
                        city,
                        area,
                        price,
                        totalOccupants,
                        description,
                        facilities.toList() ,
                        listingType,
                        listingBhk,
                        imageFiles.toList())


            }catch (e : Exception)
            {
                createSnackBar(e.message.toString())
            }

        viewModel.createNewListingResult.observe(viewLifecycleOwner){ result->
            Log.e(TAG, "onViewCreated: "+result )
            when(result)
            {
                is Result.Success ->{
                    createSnackBar(result.data)
                    binding.NewListingProgressBar.visibility=View.GONE
                    binding.newListingScreen.visibility=View.VISIBLE
                    val action = NewListingFragmentDirections.actionNewListingFragmentToMyListingsFragment()
                    findNavController().navigate(action)
                    Log.e(TAG,"onViewCreated: "+result.data)

                }

                is Result.Failure ->
                {
                    createSnackBar(result.message)
                    binding.NewListingProgressBar.visibility=View.GONE
                    binding.newListingScreen.visibility=View.VISIBLE
                    Log.e(TAG,"onViewCreated: "+result.message)

                }

                is Result.Loading ->
                {
                    createSnackBar( "Loading")
                    binding.NewListingProgressBar.visibility=View.VISIBLE
                    binding.newListingScreen.visibility=View.GONE
                    Log.e(TAG,"onViewCreated: "+"loading")

                }


            }


        }



        }









    }

    fun createSnackBar( message: String)
    {
        Snackbar.make(binding.root,message,Snackbar.LENGTH_LONG).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(requestCode, resultCode, data, this.requireActivity(), object : EasyImage.Callbacks {
            override fun onCanceled(source: MediaSource) {
                Log.e(TAG, "onCanceled: " + source)
            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                Log.e(TAG, "onImagePickerError: " )
            }

            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {

                uploadImagesAdapter.addImages(imageFiles)
                Log.e(TAG, "onMediaFilesPicked: "+imageFiles.size)
            }
        })
    }



}