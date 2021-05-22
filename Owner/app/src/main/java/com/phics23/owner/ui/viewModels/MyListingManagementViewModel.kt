package com.phics23.owner.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.owner.data.model.Tenant
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.data.repository.MyListingManagementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.phics23.owner.util.Result

@HiltViewModel
class MyListingManagementViewModel @Inject constructor(val myListingManagementRepository: MyListingManagementRepository) : ViewModel(){

    private val TAG = "MyListingManagementViewModel"

    private val _tenants: MutableLiveData<Result<List<Tenant>>> = MutableLiveData<Result<List<Tenant>>>()
    public val tenants: LiveData<Result<List<Tenant>>> by this::_tenants

    private val _bookings: MutableLiveData<List<Tenant>> = MutableLiveData<List<Tenant>>()
    public val bookings: LiveData<List<Tenant>> by this::_bookings

    private val _deleteResult: MutableLiveData<String> = MutableLiveData<String>()
    public val deleteResult: LiveData<String> by this::_deleteResult

    private val _error: MutableLiveData<String> = MutableLiveData<String>()
    public val error : LiveData<String> by this::_error

    fun getTenants(listingId: String) {
        _tenants.postValue(Result.Loading())
        viewModelScope.launch(Dispatchers.IO) {

            val tenantsResult = myListingManagementRepository.getTenants(listingId)

            when (tenantsResult) {
                is Result.Success -> {

                    val tenants = tenantsResult.data
                    Log.e(TAG, "getTenants: "+tenants )
                    _tenants.postValue(tenantsResult)
                }
                is Result.Failure -> {
                    Log.e(TAG, "getTenants:err "+tenantsResult.message )
                    _error.postValue(tenantsResult.message)
                }
            }



        }


    }

    fun deleteListing(listing: Listing) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = myListingManagementRepository.deleteListing(listing)
            if (res is Result.Success)
            {
                _deleteResult.postValue(res.data!!)
            }else _deleteResult.postValue((res as Result.Failure).message)

        }
    }


}