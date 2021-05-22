package com.phics23.tenant.data.service.listingDetail

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.phics23.tenant.data.model.Owner
import com.phics23.tenant.data.room.TenantDao
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class ListingDetailService @Inject constructor(val firebaseFirestore: FirebaseFirestore, val functions: FirebaseFunctions, val firebaseAuth: FirebaseAuth, val tenantDao: TenantDao) {

    private   val TAG = "ListingDetailService"

    suspend fun getReviews(listingId : String): Result<List<ArrayList<String>>>
    {
        var result : Result<List<ArrayList<String>>> = Result.Loading()
        try {
            val myListingIds = mutableListOf<String>()
            val reviewsDoc = firebaseFirestore.collection("Listings")
                    .document(listingId).collection("private").document("Reviews").get().await()
            if (reviewsDoc.exists()) {
                val reviewsMap  = reviewsDoc.data as MutableMap<String,ArrayList<String>>
                val reviews = reviewsMap.values.toList()

                result = Result.Success(reviews)
            }else result= Result.Failure("No reviews found")

        }catch (e : Exception)
        {
            Log.e("listingdetailservice", "getReviews: "+result,e )
            result = Result.Failure(e.message.toString())
        }

        return result
    }

    suspend fun getOwner(ownerId : String): Result<Owner>
    {
        var result : Result<Owner> = Result.Loading()
        try {
            val myListingIds = mutableListOf<String>()
            val ownerDoc = firebaseFirestore.collection("Owners")
                    .document(ownerId).get().await()
            if (ownerDoc.exists()) {
                val reviewsMap  = ownerDoc.data as MutableMap<String,ArrayList<String>>
                val owner = Owner(ownerId,
                            ownerDoc?.data!!.get("name")!!.toString(),
                            ownerDoc.data!!.get("email")!!.toString(),
                            ownerDoc.data!!.get("address")!!.toString(),
                            ownerDoc.data!!.get("phoneNumber")!!.toString(),
                            ownerDoc.data!!.get("displayImageurl") as String??:null,
                        )
                result = Result.Success(owner)
            }else result= Result.Failure("Error retriving owner information")

        }catch (e : Exception)
        {
            Log.e("listingdetailservice", "getOwner: "+result,e )
            result = Result.Failure("Error retriving owner information ")
        }

        return result
    }

    suspend fun getOwnerFcmToken(ownerId : String): String?
    {
        try
        {
            return firebaseFirestore.collection("FcmTokens").document(ownerId).get().await()["token"].toString()
        }catch (e : Exception)
        {
            Log.e(TAG, "getOwnerFcmToken: ",e )
            return null
        }
    }

    suspend fun notifyContact(token: String) {
        try {
            val tenantName = tenantDao.getTenant().name
            val data = mutableMapOf<String,String>(
                "userToken" to token,
                "title" to "Potential tenant tried contacting you!",
                "message" to "Potential tenant $tenantName tried to contact you!"
            )
            val response = functions.getHttpsCallable("notifyUser").call(data).await()
        }catch (e:Exception)
        {
            Log.e(TAG, "notifyContact: ",e )
        }

    }




}