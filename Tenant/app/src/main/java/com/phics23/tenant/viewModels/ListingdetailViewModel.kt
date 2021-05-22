package com.phics23.tenant.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.Owner
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.repository.ListingDetailRepository
import com.phics23.tenant.data.service.listingDetail.ListingDetailService
import com.phics23.tenant.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingdetailViewModel @Inject constructor(val listingDetailRepository: ListingDetailRepository) : ViewModel(){

    private lateinit var listing: Listing

    private val _owner: MutableLiveData<Owner> = MutableLiveData<Owner>()
    public val owner: LiveData<Owner> by this::_owner

    private val _rating: MutableLiveData<Float?> = MutableLiveData<Float?>()
    public val rating: LiveData<Float?> by this::_rating



    fun setListing(listing:Listing)
    {
        this.listing = listing
        listingDetailRepository.setListing(listing)
    }

    fun getListing(): Listing {
           return listingDetailRepository.getListing()
    }

    fun getDetails(listing: Listing) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = listingDetailRepository.getOwner(listing.createdBy)) {
                is Result.Success -> {
                    _owner.postValue(result.data!!)
                }
                is Result.Failure -> {
                    getDetails(listing)
                }
            }

            _rating.postValue(listingDetailRepository.getAvgRating(listing.id))

        }

    }

    fun contactOwner(owner: Owner) {
        viewModelScope.launch(Dispatchers.IO){
            listingDetailRepository.contactOwner(owner)
        }
    }


}