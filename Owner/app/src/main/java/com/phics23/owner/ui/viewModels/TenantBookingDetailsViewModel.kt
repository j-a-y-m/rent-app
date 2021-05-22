package com.phics23.owner.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.owner.data.model.Tenant
import com.phics23.owner.data.repository.MyListingManagementRepository
import com.phics23.owner.data.model.booking.Payment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.phics23.owner.util.Result

@HiltViewModel
class TenantBookingDetailsViewModel @Inject constructor(val myListingManagementRepository: MyListingManagementRepository) : ViewModel() {

    private val _previousPayments : MutableLiveData<List<Payment>> = MutableLiveData<List<Payment>>()
    public val previousPayments : LiveData<List<Payment>> by this :: _previousPayments

    private val _duePayments: MutableLiveData<List<Payment>> = MutableLiveData<List<Payment>>()
    public val duePayments: LiveData<List<Payment>> by this :: _duePayments

    private val _tenant: MutableLiveData<Tenant> = MutableLiveData<Tenant>()
    public val tenant : LiveData<Tenant> by this::_tenant

    private val _error: MutableLiveData<String> = MutableLiveData<String>()
    public val error : LiveData<String> by this::_error



    fun getBookingDetails(tenant: Tenant) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val payments = myListingManagementRepository.getPayments(tenant.bookingId)
            when (payments) {
                is Result.Success -> {
                    _previousPayments.postValue(payments.data.filter { payment -> payment.isPaid })
                    _duePayments.postValue(payments.data.filter { payment -> !payment.isPaid })
                }
                is Result.Failure -> {
                    _error.postValue(payments.message)
                }
            }


            }

    }

    fun requestCheckout(tenantId: String,message : String) {
        viewModelScope.launch(Dispatchers.IO){
            if (message.isNotEmpty()&&message.isNotBlank())
            {
                myListingManagementRepository.requestCheckout(tenantId,message)

            }else
            {
                _error.postValue("Please enter a proper message")
            }

        }
    }

    fun reportTenant(tenantId: String, reason: String) {
        viewModelScope.launch(Dispatchers.IO){
            if (reason.isNotEmpty()&&reason.isNotBlank())
            {
                myListingManagementRepository.reportTenant(tenantId,reason)

            }else
            {
                _error.postValue("Please enter a proper message")
            }

        }
    }
}