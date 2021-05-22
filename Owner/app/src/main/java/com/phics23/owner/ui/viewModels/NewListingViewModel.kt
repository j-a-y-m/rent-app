package com.phics23.owner.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.owner.data.model.listing._Listing
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.data.repository.NewListingRepository
import com.phics23.owner.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class NewListingViewModel @Inject constructor(val newListingRepository: NewListingRepository) : ViewModel()  {

    private val _createNewListingResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val createNewListingResult: LiveData<Result<String>> by this::_createNewListingResult

    fun createNewListing(
        title: String,
        address: String,
        city: String,
        area: String,
        price: String,
        totalOccupants: String,
        description: String,
        facilities: List<String>,
        listingType: String,
        listingBhk: String,
        listingImages: List<File>
    )
    {
      _createNewListingResult.postValue(Result.Loading())
      viewModelScope.launch(Dispatchers.IO)
      {
          val listing = _Listing(title, address , city, area, price , totalOccupants , description , facilities ,listingType ,listingBhk ,listingImages )
          if(validate(listing))
          {
              val listingImageUrls = uploadImages(listing.listingImages)
              when(listingImageUrls) {
                  is Result.Success ->{
                      val listingUpload = Listing(title, address , city, area, price , totalOccupants ,"", description , facilities.toList() ,listingType ,listingBhk ,listingImageUrls.data.toList(),"","" )
                      _createNewListingResult.postValue(createListing(listingUpload))
                  }
                  is Result.Failure -> {
                      Log.e("TAG", "createNewListing:listingimageurls "+listingImageUrls.message )
                      _createNewListingResult.postValue(Result.Failure(listingImageUrls.message))
                  }
              }


          }

      }


    }



    private suspend fun validate(listing: _Listing): Boolean
    {

        if(listing.title.isEmpty() || listing.address.isEmpty() || listing.city.isEmpty() || listing.area.isEmpty() || listing.price.isEmpty() || listing.totalOccupants.isEmpty() || listing.description.isEmpty() || listing.facilities.isEmpty() || listing.listingType.isEmpty() || listing.listingBhk.isEmpty() || listing.listingImages.isEmpty())
        {
            _createNewListingResult.postValue(Result.Failure("Please enter all details"))
            return false
        }else
        if(listing.description.length<60)
        {
            _createNewListingResult.postValue(Result.Failure("Please enter detailed description"))
            return false
        }else
        if (listing.listingImages.size<3)
        {
            _createNewListingResult.postValue(Result.Failure("Please add more pictures"))
            return false
        }else
        {
            return true
        }



    }

    suspend private fun uploadImages(imageFiles : List<File>): Result<List<String>> {
        return newListingRepository.uploadImages(imageFiles)
    }

    private suspend fun createListing(listing: Listing): Result<String> {
        return newListingRepository.submitListing(listing)
    }

}