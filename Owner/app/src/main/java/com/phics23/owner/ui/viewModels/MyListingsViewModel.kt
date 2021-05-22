package com.phics23.owner.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phics23.owner.data.repository.MyListingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltViewModel
class MyListingsViewModel @Inject constructor(val myListingsRepository: MyListingsRepository) : ViewModel(){

    private val _myListingsResult: MutableLiveData<Result<List<Listing>>> = MutableLiveData<Result<List<Listing>>>()
    public val myListingsResult: LiveData<Result<List<Listing>>> by this::_myListingsResult



    fun getMyListings()
    {   _myListingsResult.postValue(Result.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            _myListingsResult.postValue(myListingsRepository.getMyListings())
        }

    }
}