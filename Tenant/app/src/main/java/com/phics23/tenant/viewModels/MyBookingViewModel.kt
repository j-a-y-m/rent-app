package com.phics23.tenant.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.tenant.data.model.Owner
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.repository.MyBookingRepository
import com.phics23.tenant.data.repository.PaymentRepository
import com.phics23.tenant.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyBookingViewModel @Inject constructor(val myBookingRepository: MyBookingRepository, val paymentRepository: PaymentRepository) : ViewModel() {

    private val _previousPayments: MutableLiveData<List<Payment>> = MutableLiveData<List<Payment>>()
    public val previousPayments: LiveData<List<Payment>> by this::_previousPayments

    private val _duePayments: MutableLiveData<List<Payment>> = MutableLiveData<List<Payment>>()
    public val duePayments: LiveData<List<Payment>> by this::_duePayments

    private val _listing: MutableLiveData<Listing> = MutableLiveData<Listing>()
    public val listing : LiveData<Listing> by this::_listing

    private val _owner: MutableLiveData<Owner> = MutableLiveData<Owner>()
    public val owner : LiveData<Owner> by this::_owner

    private val _hasBooking: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    public val hasBooking : LiveData<Boolean> by this::_hasBooking

    private val _cancelBookingRequest: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val cancelBookingRequest : LiveData<Result<String>> by this::_cancelBookingRequest

    private val _error: MutableLiveData<String> = MutableLiveData<String>()
    public val error : LiveData<String> by this::_error

    fun getBookingDetails() {
        viewModelScope.launch(Dispatchers.IO)
        {
            if (myBookingRepository.hasBooking())
            {
                paymentRepository.updateCurrentPayments()
                _hasBooking.postValue(true)
                val booking = myBookingRepository.getCurrentBooking()
                val listing = myBookingRepository.getListing(booking.listingId)
                val owner  = myBookingRepository.getOwner(booking.ownerId)
                if (listing is Result.Failure || owner is Result.Failure)
                {
                    _error.postValue("Couldnt retrive results")
                    return@launch
                }
                _listing.postValue((listing as Result.Success).data!!)
                _owner.postValue((owner as Result.Success).data!!)
                _previousPayments.postValue(myBookingRepository.getPreviousPayments(booking.bookingId))
                _duePayments.postValue(myBookingRepository.getDuePayments(booking.bookingId))
            }else
            {
                _hasBooking.postValue(false)
            }
        }

    }

    fun cancelBooking() {
        viewModelScope.launch(Dispatchers.IO){
            paymentRepository.updateCurrentPayments()

             _cancelBookingRequest.postValue(myBookingRepository.cancelBooking())

        }
    }

    fun submitRating(rating: Float, reviewText: String, listing: Listing) {
        viewModelScope.launch(Dispatchers.IO){
            if (rating == 0f)
            {
                _error.postValue("Please give rating")
            }else
            {   val res : Result<String> = myBookingRepository.submitRating(rating, reviewText,listing)
                if ( res is Result.Success<String>)
                {

                }else
                {
                    _error.postValue((res as Result.Failure<String>).message)
                }

            }
        }

    }


}