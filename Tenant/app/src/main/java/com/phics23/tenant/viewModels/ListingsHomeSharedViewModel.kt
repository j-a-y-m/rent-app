package com.phics23.tenant.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel


class ListingsHomeSharedViewModel :ViewModel(){

    private val TAG = "ListingsHomeSharedViewM"
    
//    private val _filter: MutableLiveData<MutableMap<String, String>> = Mut`ableLiveData<MutableMap<String, String>>()
//    public val filter : LiveData<MutableMap<String, String>> by this::_filter

    private val _filter: SingleLiveEvent<MutableMap<String, String>> = SingleLiveEvent<MutableMap<String, String>>()
    public val filter : LiveData<MutableMap<String, String>> by this::_filter

    fun setFilter(filter: MutableMap<String, String>) {
        _filter.value=filter
    }
}