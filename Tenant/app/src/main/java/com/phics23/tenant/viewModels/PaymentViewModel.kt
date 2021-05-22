package com.phics23.tenant.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.repository.PaymentRepository
import com.phics23.tenant.data.service.booking.DbTest
import com.phics23.tenant.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(val paymentRepository: PaymentRepository,
                                           val auth: FirebaseAuth, val dbTest: DbTest) : ViewModel(){


    private lateinit var listing: Listing

    private val _payment: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val payment: LiveData<Result<String>> by this::_payment

    private val _newBooking: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val newBooking: LiveData<Result<String>> by this::_newBooking


    public fun testDb()
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            //dbTest.addDuePayment()
        }



    }




    fun createNewBooking(listing : Listing) {

        viewModelScope.launch(Dispatchers.IO)
        {
            _newBooking.postValue(paymentRepository.newBooking(listing))
        }

    }

    fun makePayment(payment: Payment) {
        viewModelScope.launch(Dispatchers.IO)
        {
            _payment.postValue(paymentRepository.makePayment(payment))
        }
    }

//
//    fun setListing(listing:Listing)
//    {
//        this.listing = listing
//        listingDetailRepository.setListing(listing)
//    }
//
//    fun getListing(): Listing {
//           return listingDetailRepository.getListing()
//    }
//
//    fun getDetails(listing: Listing) {
//        viewModelScope.launch(Dispatchers.IO) {
//            when (val result = listingDetailRepository.getOwner(listing.createdBy)) {
//                is Result.Success -> {
//                    _owner.postValue(result.data!!)
//                }
//                is Result.Failure -> {
//                    getDetails(listing)
//                }
//            }
//
//            _rating.postValue(listingDetailRepository.getAvgRating(listing.id))
//
//        }
//
//    }
//

}