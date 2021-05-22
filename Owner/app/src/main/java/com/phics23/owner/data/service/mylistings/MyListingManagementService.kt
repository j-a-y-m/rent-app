package com.phics23.owner.data.service.mylistings

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.phics23.owner.data.model.booking.Transaction
import com.phics23.owner.data.model.booking.Booking
import com.phics23.owner.data.model.booking.Payment
import com.phics23.owner.data.model.Tenant
import com.phics23.owner.data.room.OwnerDao
import com.phics23.owner.util.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MyListingManagementService @Inject constructor(val firestore: FirebaseFirestore, val auth: FirebaseAuth, val functions: FirebaseFunctions, val ownerDao: OwnerDao){

    private val TAG = "MyListingManagementServ"

    val ownerUid = auth.uid

    suspend fun getTenants(listingId: String): Result<List<Tenant>> {

        try {
            val bookings = firestore.collection("Bookings")
                    .whereEqualTo("listingId",listingId)
                    .whereEqualTo("ownerId",ownerUid)
                    .whereEqualTo("isActive",true)
                    .get().await().map { doc->
                       Booking( doc.id,
                               doc?.get("listingId").toString(),
                               doc?.get("ownerId").toString(),
                               doc?.get("tenantId").toString(),
                               doc?.get("startDate").toString().toFloat().toLong(),
                               doc?.get("isActive").toString().toBoolean(),
                               doc?.get("isDue").toString().toBoolean())
                    }
            Log.e(TAG, "getTenants: $bookings", )
            val tenants = bookings.flatMap{ booking->
                        firestore.collection("Tenants")
                        .whereEqualTo("id",booking.tenantId).get().await().map { tenantDoc->

                                    Tenant(tenantDoc.id,
                                            tenantDoc["email"].toString(),
                                            tenantDoc["name"].toString(),
                                            tenantDoc["address"].toString(),
                                            tenantDoc["phoneNumber"].toString(),
                                            tenantDoc["displayPicture"]?.toString(),
                                            booking.bookingId,
                                            booking.listingId,
                                            booking.ownerId,
                                            booking.startDate,
                                            booking.isActive,
                                            booking.isDue)

                        }
            }
            Log.e(TAG, "getTenants: "+tenants )
            return Result.Success(tenants)

        }catch (e : Exception)
        {
            Log.e(TAG, "getTenants: ",e )
            return Result.Failure(e.message.toString())
        }
    }


    suspend fun getBooking(tenantId: String, listingId:String): Result<Booking> {
        try {
            val booking =firestore.collection("Bookings")
                    .whereEqualTo("listintgId",listingId)
                    .whereEqualTo("tenantId",tenantId)
                    .whereEqualTo("isActive",true)
                    .get().await().map { bookingDoc->
                            Booking(bookingDoc["bookingId"].toString(),
                                    bookingDoc["listingId"].toString(),
                                    bookingDoc["ownerId"].toString(),
                                    bookingDoc["tenantId"].toString(),
                                    bookingDoc["startDate"].toString().toFloat().toLong(),
                                    bookingDoc["isActive"].toString().toBoolean(),
                                    bookingDoc["isDue"].toString().toBoolean())


            }.get(0)
            return Result.Success(booking)
        }catch (e:Exception){
            return Result.Failure(e.message.toString())
        }
    }


    suspend fun getPayments(bookingId: String): Result<List<Payment>> {
        try {
            Log.e(TAG, "getPayments: $bookingId")
            val payments =firestore.collection("Bookings")
                    .document(bookingId)
                    .collection("Payments")
                    .get().await().map { paymentDoc->
                        Payment(paymentDoc["paymentId"].toString(),
                                paymentDoc["pcStartDate"].toString().toFloat().toLong(),
                                paymentDoc["pcEndDate"].toString().toFloat().toLong(),
                                paymentDoc["amount"].toString(),
                                paymentDoc["isDue"].toString().toBoolean(),
                                paymentDoc["isPaid"].toString().toBoolean(),
                                paymentDoc["transactionId"]?.toString(),
                                paymentDoc["paidDate"]?.toString()?.toFloat()?.toLong(),
                                paymentDoc["bookingId"].toString())


                    }
            return Result.Success(payments)
        }catch (e:Exception){
            Log.e(TAG, "getPayments:err ",e )
            return Result.Failure(e.message.toString())
        }
    }


    suspend fun getTransactions(bookingId: String): Result<List<Transaction>> {
        try {
            val transactions = firestore.collection("Bookings")
                    .document(bookingId)
                    .collection("Transactions")
                    .get().await().map { transactionDoc ->
                        Transaction(transactionDoc["transactionId"].toString(),
                                transactionDoc["transactionDate"].toString().toFloat().toLong(),
                                transactionDoc["amount"].toString(),
                                transactionDoc["pcStartDate"].toString().toFloat().toLong(),
                                transactionDoc["pcEndDate"].toString().toFloat().toLong(),
                                transactionDoc["paymentId"].toString(),
                                transactionDoc["bookingId"].toString())


                    }
            return Result.Success(transactions)
        } catch (e: Exception) {
            return Result.Failure(e.message.toString())
        }

    }

    suspend fun deleteListing(listingId: String): Result<String> {


        return try{
            firestore.collection("Listings").document(listingId).delete().await()
            Result.Success("Listing Deleted")
        }catch (e : Exception) {
            Result.Failure(e.message.toString())
        }
        TODO("move this to cloud functions")
    }

    suspend fun getTenantToken(tenantId: String): String? {
            try
            {
                Log.e(TAG, "getTenantToken:tid "+tenantId )
                val token = firestore.collection("FcmTokens").document(tenantId).get().await()["token"].toString() //.get().await().data?.get("token").toString() //.data?.get("token").toString()
                    Log.e(TAG, "getTenantToken: "+token )
                return token

            }catch (e :Exception)
            {
                Log.e(TAG, "getTenantFcmToken: ",e )
                return null
            }

    }

    suspend fun notifyCheckoutRequest(token: String, message: String) {
        try {

            val data = mutableMapOf<String,String>(
                "userToken" to token,
                "title" to "Your landlord has requested you to checkout",
                "message" to message
            )
            val response = functions.getHttpsCallable("notifyUser").call(data).await()
        }catch (e: Exception)
        {
            Log.e(TAG, "notifyCheckoutRequest: ",e )
        }

    }

    suspend fun reportTenant(tenantId: String, reason: String) {
        try {
            val reportDocRef = firestore.collection("Reports").document()

            val data = mutableMapOf<String,String>(
                "reportBy" to auth.uid!!,
                "reportedAgainst" to tenantId,
                "message" to reason,
                "reportId" to reportDocRef.id
            )

            reportDocRef.set(data).await()
        }catch (e : java.lang.Exception)
        {
            Log.e(TAG, "reportTenant: ",e )
        }


    }

}

/*
* rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    //match /Listings/{listing_id} {
     // allow write: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid));
    //}
    match /Listings/{listing_id}/private/Status {
      allow create: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow update: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if true
  }
   match /Listings/{listing_id} {
      allow create: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow update: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      //allow write: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow delete: if resource.data.createdBy == request.auth.uid;
      allow read: if true

  }

  match /Cities/{city_name} {
      //allow create: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow write: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if true

  }

  match /Cities/{city_name}/listings/{listing_id} {
      //allow create: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow write: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if true

  }

  match /ListingRequests/{listing_id} {
      allow create: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid));
      allow update: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow delete: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid)) || exists(/databases/$(database)/documents/Admins/$(request.auth.uid)) || exists(/databases/$(database)/documents/Tenants/$(request.auth.uid)) ;
    }

  match /Owners/{user_id} {
      allow write: if user_id == request.auth.uid;
      allow read: if true
    }

   match /Owners/{user_id}/private/details {
   		allow create: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid)) || exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow update: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid)) || exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid)) || exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
   }

   match /Owners/{user_id}/listings/{listing_id} {
   		allow create: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow update: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow delete: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid)) || exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
   }

  match /Tenants/{tenant_id} {
      allow write: if tenant_id == request.auth.uid;
      allow update: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow delete: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      //allow read: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid)) && get(/databases/$(database)/documents/Listings/$(listing_id)).data.createdBy==request.auth.uid
      allow read: if true
    }

    // match /Listings/{listing_id}/private/{Status} {
    //   allow create: if exists(/databases/$(database)/documents/Owners/$(request.auth.uid));
    //   allow create: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
    //   allow read: if true
    // }



    //****///payments
    match /Bookings/{bookingId} {
      allow write: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if resource.data.tenantId == request.auth.uid || resource.data.ownerId == request.auth.uid || exists(/databases/$(database)/documents/Admins/$(request.auth.uid));

      match /Transactions/{transactionId} {
      allow write: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if get(/databases/$(database)/documents/Bookings/$(bookingId)).data.tenantId == request.auth.uid || get(/databases/$(database)/documents/Bookings/$(bookingId)).data.ownerId == request.auth.uid || exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      }

       match /Payments/{paymentId} {
      allow write: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read: if get(/databases/$(database)/documents/Bookings/$(bookingId)).data.tenantId == request.auth.uid || get(/databases/$(database)/documents/Bookings/$(bookingId)).data.ownerId == request.auth.uid || exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      }
		}


    ///tokens
      match /FcmTokens/{id} {
       allow create: if id == request.auth.uid;
       allow update: if id == request.auth.uid;
       //allow read: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
       allow read: if request.auth != null ;
      }

      //reports
      match /Reports/{reportID}{
      allow create:  if request.auth != null ;
      allow update: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow delete: if exists(/databases/$(database)/documents/Admins/$(request.auth.uid));
      allow read:  if request.auth != null ;
      }


  }
}
*
* */