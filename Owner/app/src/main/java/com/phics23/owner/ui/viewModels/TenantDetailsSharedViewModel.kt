package com.phics23.owner.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phics23.owner.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class TenantDetailsSharedViewModel : ViewModel() {
    private val _message: SingleLiveEvent<String> = SingleLiveEvent<String>()
    public val message : LiveData<String> by this::_message

    private val _reportReason: SingleLiveEvent<String> = SingleLiveEvent<String>()
    public val reportReason : LiveData<String> by this::_reportReason



    fun requestCheckout(message : String) {
        _message.value = message
    }

    fun reportReason(reason: String) {
        _reportReason.value = reason
    }
}