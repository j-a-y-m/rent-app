package com.phics23.tenant.data.repository

import android.util.Log
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.room.booking.BookingDao
import com.phics23.tenant.data.service.booking.BookingService
import com.phics23.tenant.util.Result
import javax.inject.Inject

class PaymentRepository @Inject constructor(val bookingDao : BookingDao ,val bookingService: BookingService) {
    private  val TAG = "PaymentRepository"
    private var currentListingId : String? = null
    private lateinit var listing: Listing
    private var reviews : List<ArrayList<String>>? = null





    fun setListing(listing: Listing) {
        this.listing=listing
    }

    fun getListing(): Listing {
        return this.listing
    }

    suspend fun hasBooking(): Boolean {
        return bookingDao.hasBooking()
    }

    fun doTransaction(payment: Payment) {

        if (payment.bookingId==null)
        {
//            bookingService.newBooking(payment)
//            {
//                //
//            }
        }

    }

    suspend fun newBooking(listing: Listing): Result<String> {
        var result : Result<String> = Result.Loading()
        if(bookingDao.hasBooking())
        {
            return Result.Failure("You already have a booking")
        }
        val newBookingRequestResult : Result<Map<String,String>> = bookingService.newBookingRequest(listing)

        when (newBookingRequestResult) {
            is Result.Success -> {
                Log.e(TAG, "newBooking: "+newBookingRequestResult.data.toString() )
                val bookingResult = bookingService.getBooking(newBookingRequestResult.data["bookingId"]!!)
                val paymentResult = bookingService.getPayment(newBookingRequestResult.data["paymentId"]!!, newBookingRequestResult.data["bookingId"]!!)
                val transactionResult = bookingService.getTransaction(newBookingRequestResult.data["transactionId"]!!, newBookingRequestResult.data["bookingId"]!!)
                if (bookingResult is Result.Failure || paymentResult is Result.Failure || transactionResult is Result.Failure) {
                    Log.e(TAG, "newBooking: Error fetching new booking details")
                    return Result.Failure("Error fetching new booking details")
                } else {
                    bookingDao.insertBooking((bookingResult as Result.Success).data)
                    bookingDao.insertPayment((paymentResult as Result.Success).data)
                    bookingDao.insertTransaction((transactionResult as Result.Success).data)
                    return Result.Success("Transaction successful")
                }
            }
            is Result.Failure -> {
                result  = Result.Failure(newBookingRequestResult.message)
            }
        }

        return result
    }

    suspend fun makePayment(payment: Payment): Result<String> {
        var result : Result<String> = Result.Loading()
        val bookingId  = payment.bookingId
        val listingId = bookingDao.getBooking(bookingId).listingId
        val paymentRequestResult = bookingService.makePayment(payment,listingId,bookingId)

        when (paymentRequestResult) {
            is Result.Success -> {
                val transactionResult = bookingService.getTransaction(paymentRequestResult.data["transactionId"]!!, bookingId)
                when (transactionResult) {
                    is Result.Success -> {
                        val transaction = transactionResult.data
                        bookingDao.insertTransaction(transaction)
                        val payment =  bookingDao.getPayment(payment.paymentId)
                        val updatedPayment = Payment(payment.paymentId,
                                                payment.pcStartDate,
                                                payment.pcEndDate,
                                                payment.amount,
                                                false,true,transaction.transactionId,transaction.transactionDate,
                                                bookingId)
                        bookingDao.updatePayment(updatedPayment)
                        updateCurrentPayments()
                        return Result.Success("Transaction successful")
                    }
                    is Result.Failure -> {
                        result  = Result.Failure(transactionResult.message)
                    }
                }
            }
            is Result.Failure -> {
                result = Result.Failure(paymentRequestResult.message)
            }
        }

        return result
    }

    suspend fun updateCurrentPayments()
    {
        if(bookingDao.hasBooking())
        {
            val bookingId = bookingDao.getCurrentBooking().bookingId
            val payments = bookingDao.getPayments(bookingId)
            val paymentIds = payments.map { payment ->   payment.paymentId }
            val updatedPaymentsResult = bookingService.getPayments(bookingId)
            when (updatedPaymentsResult) {
                is Result.Success -> {
                    if (updatedPaymentsResult.data.isNotEmpty())
                    {

                        bookingDao.insertPayments(updatedPaymentsResult.data)
                    }
                }
                is Result.Failure -> {
                    Log.e(TAG, "updateCurrentPayments: "+updatedPaymentsResult.message )
                }
            }

        }

    }

    suspend fun updatePayments(bookingId : String) {
        val payments = bookingDao.getPayments(bookingId)
        val paymentIds = payments.map { payment ->   payment.paymentId }
        val updatedPayments : List<Payment> = bookingService.updatePayments(paymentIds,bookingId)
        if (updatedPayments.isNotEmpty())
        {
            bookingDao.insertPayments(updatedPayments)
        }
    }


}