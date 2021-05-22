package com.phics23.tenant.data.service.booking

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Booking
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.model.payments.Transaction
import com.phics23.tenant.data.room.booking.BookingDao
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class BookingService @Inject constructor(val bookingDao : BookingDao, val auth: FirebaseAuth, val firestore: FirebaseFirestore, val functions: FirebaseFunctions) {

    private val uid = auth.uid!!
    private val TAG = "BookingService"

    suspend fun newBookingRequest(listing: Listing): Result<Map<String, String>> {
            var result : Result<Map<String,String>> = Result.Loading()

        val data = hashMapOf(
                "tenantId" to uid,
                "listingId" to listing.id
        )

        val response = functions.getHttpsCallable("newBooking-newBooking").call(data).await()
         if (response.data != null)
         {
             val responseData = response.data as HashMap<String,String>
             if (responseData["error"] == null)
             {
                 result = Result.Success(responseData)
             }else
             {
                 result = Result.Failure(responseData["error"]!!)
             }
         }else
         {
             result = Result.Failure("error occured")
         }

        return result
    }

    suspend fun makePayment(payment: Payment, listingId : String, bookingId: String): Result<Map<String,String>> {
        var result : Result<Map<String,String>> = Result.Loading()
        val data = hashMapOf(
                "tenantId" to uid,
               "listingId" to listingId,
                "paymentId" to payment.paymentId,
                "bookingId" to bookingId,
        )

        try {
            val response = functions.getHttpsCallable("makePayment").call(data).await()
            val responseData = response.data as? HashMap<String,String>
            if (responseData != null)
            {
                if (data["error"] == null)
                {
                    result = Result.Success(responseData)
                }else
                {
                    result = Result.Failure(responseData["error"]!!)
                }
            }else
            {
                result = Result.Failure("error occured")
            }
        }catch (e : Exception)
        {
            result = Result.Failure(e.message.toString())
        }


        return result


    }

    suspend fun getBooking(bookingId : String): Result<Booking> {
        try {
            val data = firestore.collection("Bookings")
                    .document(bookingId).get().await().data
            val booking : Booking = Booking(bookingId,
                    data?.get("listingId").toString(),
                    data?.get("ownerId").toString(),
                    data?.get("startDate").toString().toFloat().toLong(),
                    data?.get("isActive") as Boolean,
                        )
            return Result.Success(booking)
        }catch (e:Exception)
        {

            Log.e(TAG, "getBooking: "+bookingId,e )
            return Result.Failure(e.message.toString())
        }


    }

    suspend fun getPayment(paymentId: String, bookingId : String): Result<Payment> {

        try {
            val data = firestore.collection("Bookings")
                                .document(bookingId).collection("Payments").document(paymentId).get().await().data
            val payment : Payment = Payment(paymentId,
                                    data?.get("pcStartDate").toString().toFloat().toLong(),
                                    data?.get("pcEndDate").toString().toFloat().toLong(),
                                    data?.get("amount").toString(),
                                    data?.get("isDue") as Boolean,
                                    data.get("isPaid") as Boolean,
                                    data.get("transactionId")?.toString(),
                                    data.get("paidDate")?.toString()?.toFloat()?.toLong(),
                                    bookingId)
            return Result.Success(payment)
        }catch (e:Exception)
        {
            Log.e(TAG, "getPayment: ",e )
            return Result.Failure(e.message.toString())
        }


    }

    suspend fun getPayments(bookingId : String): Result<List<Payment>> {

        try {
            val documents = firestore.collection("Bookings")
                    .document(bookingId).collection("Payments").get().await()
            if (documents != null) {
                val payments = documents.map { document->
                    val data  = document.data
                    return@map Payment(document.id,
                            data.get("pcStartDate").toString().toFloat().toLong(),
                            data.get("pcEndDate").toString().toFloat().toLong(),
                            data.get("amount").toString(),
                            data.get("isDue") as Boolean,
                            data.get("isPaid") as Boolean,
                            data.get("transactionId")?.toString(),
                            data.get("paidDate")?.toString()?.toFloat()?.toLong(),
                            bookingId)

                }
                return Result.Success(payments)
            }else return Result.Failure("couldnt retrive payments")


        }catch (e:Exception)
        {
            Log.e(TAG, "getPayment: ",e )
            return Result.Failure(e.message.toString())
        }


    }

    suspend fun getTransaction(transactionId: String, bookingId : String): Result<Transaction> {

        try {
            val data = firestore.collection("Bookings")
                    .document(bookingId).collection("Transactions").document(transactionId).get().await().data
            val transaction : Transaction = Transaction(transactionId,
                    data?.get("transactionDate").toString().toFloat().toLong(),
                    data?.get("amount").toString(),
                    data?.get("pcStartDate").toString().toFloat().toLong(),
                    data?.get("pcEndDate").toString().toFloat().toLong(),
                    data?.get("paymentId").toString(),
                    bookingId)
            return Result.Success(transaction)
        }catch (e:Exception)
        {
            Log.e(TAG, "getTransaction: ",e )
            return Result.Failure(e.message.toString())
        }
    }

    suspend fun updatePayments(paymentIds: List<String>, bookingId: String): List<Payment> {

        try {
            val paymentsQuery = firestore.collection("Bookings")
                    .document(bookingId).collection("Payments")
                    .whereNotIn("paymentId",paymentIds).get().await()

            if (!paymentsQuery.isEmpty)
            {
                val payments = paymentsQuery.documents.map { doc->
                       val data = doc.data
                        return@map  Payment(data?.get("paymentId").toString(),
                            data?.get("pcStartDate").toString().toFloat().toLong(),
                            data?.get("pcEndDate").toString().toFloat().toLong(),
                            data?.get("amount").toString(),
                            data?.get("isDue") as Boolean,
                            data.get("isPaid") as Boolean,
                            data.get("transactionId") as? String,
                            data.get("paidDate")?.toString()?.toFloat()?.toLong(),
                            bookingId)


                }
                return payments
            }else
            {
                return listOf<Payment>()
            }
        }catch (e : Exception)
        {
            return listOf<Payment>()
        }


    }

    suspend fun getTransactions( bookingId: String): List<Transaction> {

        try {
            val transactionsDocs = firestore.collection("Bookings")
                    .document(bookingId).collection("Transactions")
                    .get().await()

            if (!transactionsDocs.isEmpty)
            {
                val transactions = transactionsDocs.documents.map { doc->
                    val data = doc.data
                    return@map Transaction(data?.get("transactionId").toString(),
                            data?.get("transactionDate").toString().toFloat().toLong(),
                            data?.get("amount").toString(),
                            data?.get("pcStartDate").toString().toFloat().toLong(),
                            data?.get("pcEndDate").toString().toFloat().toLong(),
                            data?.get("paymentId").toString(),
                            bookingId)


                }
                return transactions
            }else
            {
                return listOf<Transaction>()
            }
        }catch (e : Exception)
        {
            return listOf<Transaction>()
        }


    }

    suspend fun hasBooking(): Boolean {
       try {

         return  !firestore.collection("Bookings").whereEqualTo("tenantId",uid).whereEqualTo("isActive",true).get().await().isEmpty
       }catch (e:Exception)
       {
           Log.e(TAG, "hasBooking: ",e )
           return false
       }

    }

    suspend fun getCurrentBooking() : Booking?{
        try {

            val bookingData = firestore.collection("Bookings").whereEqualTo("tenantId",uid).whereEqualTo("isActive",true).get().await().documents[0].data
            val bookingId = firestore.collection("Bookings").whereEqualTo("tenantId",uid).whereEqualTo("isActive",true).get().await().documents[0].id
            Log.e(TAG, "getCurrentBooking: "+bookingData )
            return Booking(bookingId,bookingData?.get("listingId").toString(),bookingData?.get("ownerId").toString(),bookingData?.get("startDate").toString().toFloat().toLong(),bookingData?.get("isActive") as Boolean)
        }catch (e:Exception)
        {
            Log.e(TAG, "hasBooking: ",e )
            return null
        }
    }

    suspend fun cancelBooking(bookingId: String): Result<String> {
        val data = hashMapOf(
            "tenantId" to uid,
            "bookingId" to bookingId
        )
        Log.e(TAG, "cancelBooking: "+data )
        val response = functions.getHttpsCallable("cancelBooking").call(data).await()
        if (response.data != null)
        {
            val responseData = response.data as HashMap<String,String>
            if (responseData["error"] == null)
            {
                return Result.Success("Booking Cancelled")
            }else
            {
                Log.e(TAG, "cancelBooking: "+responseData["error"] )
                return Result.Failure(responseData["error"]!!)
            }
        }else
        {
            return Result.Failure("error occured")
        }


    }

    suspend fun submitRating(rating: Float, reviewText: String, listing: Listing): Result<String> {

        try {
            if (bookingDao.getCurrentBooking().listingId!=listing.id)
            {
                return Result.Failure("You cannot rate this property")
            }else
            {
                val data = mutableMapOf<String,String>(
                    "rating" to rating.toString(),
                    "review" to reviewText
                )
                firestore.collection("Listings").document(listing.id).collection("Reviews").document().set(data)
                firestore.collection("Bookings").document(bookingDao.getCurrentBooking().bookingId).collection("TenantPrivate").document("isReviewed").set(
                    mutableMapOf("isReviewed" to true))
                return Result.Success("rating submitted")
            }
        }catch (e : Exception)
        {
            return Result.Failure(e.message.toString())
        }


    }


}