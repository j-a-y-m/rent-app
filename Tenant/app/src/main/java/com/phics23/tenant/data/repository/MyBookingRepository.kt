package com.phics23.tenant.data.repository

import android.util.Log
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Booking
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.model.Owner
import com.phics23.tenant.data.room.booking.BookingDao
import com.phics23.tenant.data.room.booking.BookingDatabase
import com.phics23.tenant.data.service.booking.BookingService
import com.phics23.tenant.data.service.listingDetail.ListingDetailService
import com.phics23.tenant.data.service.listingsHome.listingsService
import com.phics23.tenant.util.Result
import javax.inject.Inject

class MyBookingRepository @Inject constructor(val bookingDao: BookingDao,val bookingService: BookingService, val listingsService: listingsService, val  listingDetailService: ListingDetailService,val bookingDatabase: BookingDatabase) {

    private val TAG = "MyBookingRepository"

    var payments : List<Payment>? = null

    suspend fun hasBooking(): Boolean {
        if (bookingDao.hasBooking())
        {
            return true
        }else
        {
            if(bookingService.hasBooking())
            {
                val booking = bookingService.getCurrentBooking()
                if (booking != null) {
                    Log.e(TAG, "hasBooking: "+booking.toString() )
                    Log.e(TAG, "hasBooking: "+booking.bookingId )
                    val payments = bookingService.getPayments(booking.bookingId)
                    if(payments is Result.Failure) return false
                    val transactions = bookingService.getTransactions(booking.bookingId)
                    bookingDao.insertBooking(booking)
                    bookingDao.insertPayments((payments as Result.Success).data)
                    bookingDao.insertTransactions(transactions)

                    return true
                }else return false
            }
            else return false
        }


    }

    suspend fun getCurrentBooking(): Booking {
         return bookingDao.getCurrentBooking()
    }

    suspend fun getListing(listingId: String): Result<Listing> {
        //val listingResponse = listingsService.getListing(listingId)
        return  listingsService.getListing(listingId)
    }

    suspend fun getOwner(ownerId: String): Result<Owner> {

        return  listingDetailService.getOwner(ownerId)
    }

    suspend fun getPreviousPayments(bookingId: String): List<Payment> {
        return bookingDao.getPreviousPayments(bookingId)
    }

    suspend fun getDuePayments(bookingId: String): List<Payment> {
        return bookingDao.getDuePayments(bookingId)
    }

    suspend fun getPayments(bookingId: String) :  Result<List<Payment>> {
        if (payments==null){
            val paymentRes = bookingService.getPayments(bookingId)
            when (paymentRes) {
                is  Result.Success -> {
                    payments = paymentRes.data
                    return  Result.Success(paymentRes.data)
                }
                is  Result.Failure -> {
                    Log.e("listingmanagementrepo", "getPayments: "+paymentRes.message )
                    return  Result.Failure(paymentRes.message)
                }
                is  Result.Loading->  {
                    return  Result.Loading()
                }
            }
        }else
        {
            return  Result.Success(payments!!)
        }
    }

    suspend fun cancelBooking() : Result<String> {
        val booking = getCurrentBooking()
        val duePayments = getDuePayments(booking.bookingId).filter { payment -> payment.isDue }
        if(duePayments.isNotEmpty())
        {
            return Result.Failure("Please clear previous payments before cancelling")
        }else
        {
            bookingDatabase.clearAllTables()
            return bookingService.cancelBooking(booking.bookingId)
        }


    }

    suspend fun submitRating(rating: Float, reviewText: String, listing: Listing): Result<String> {

        return bookingService.submitRating(rating,reviewText,listing)

    }
}