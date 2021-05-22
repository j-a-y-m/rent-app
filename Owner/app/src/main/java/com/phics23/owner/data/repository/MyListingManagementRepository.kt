package com.phics23.owner.data.repository

import android.util.Log
import com.phics23.owner.data.model.booking.Booking
import com.phics23.owner.data.model.booking.Payment
import com.phics23.owner.data.model.Tenant
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.data.service.mylistings.MyListingManagementService
import com.phics23.owner.util.Result
import javax.inject.Inject
import kotlin.math.log10

class MyListingManagementRepository @Inject constructor(val myListingManagementService: MyListingManagementService) {

    var payments : List<Payment>? = null

    suspend fun getTenants(listingId: String): Result<List<Tenant>> {

       return myListingManagementService.getTenants(listingId)

    }

    suspend fun getBooking(tenantId:String, listingId: String): Result<Booking> {
        return myListingManagementService.getBooking(tenantId,listingId)
    }


    suspend fun getPayments(bookingId: String): Result<List<Payment>> {
        if (payments==null){
            val paymentRes = myListingManagementService.getPayments(bookingId)
            when (paymentRes) {
                is Result.Success -> {
                    payments = paymentRes.data
                    return Result.Success(paymentRes.data)
                }
                is Result.Failure -> {
                    Log.e("listingmanagementrepo", "getPayments: "+paymentRes.message )
                    return Result.Failure(paymentRes.message)
                }
                is Result.Loading->  {
                return Result.Loading()
            }
            }
        }else
        {   
            return Result.Success(payments!!)
        }
    }

    suspend fun deleteListing(listing: Listing): Result<String> {

       return myListingManagementService.deleteListing(listing.id)
    }

    suspend fun requestCheckout(tenantId: String,message : String) {
     val tenantToken = myListingManagementService.getTenantToken(tenantId)
        if (tenantToken != null) {

            myListingManagementService.notifyCheckoutRequest(tenantToken,message)
        }
    }

    suspend fun reportTenant(tenantId: String, reason: String) {
        myListingManagementService.reportTenant(tenantId, reason)
    }

}