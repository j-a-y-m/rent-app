package com.phics23.owner.data.service.mylistings

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class MyListingsService @Inject constructor(val firebaseFirestore: FirebaseFirestore, val firebaseAuth: FirebaseAuth) {
    private  val TAG = "MyListingsService"
    private val uid = firebaseAuth.currentUser!!.uid
    suspend fun getMyListings() : Result<List<Listing>>
    {   var result : Result<List<Listing>> = Result.Loading()

        try {

            val mylistingDocs = firebaseFirestore.collection("Listings").whereEqualTo("createdBy",firebaseAuth.uid).get().await()

            val listings = mylistingDocs.map { listingDoc->

                Listing(listingDoc!!.data!!.get("title")!!.toString(),
                    listingDoc.data!!.get("address")!!.toString(),
                    listingDoc.data!!.get("city")!!.toString(),
                    listingDoc.data!!.get("area")!!.toString(),
                    listingDoc.data!!.get("price")!!.toString(),
                    listingDoc.data!!.get("totalOccupants")!!.toString(),
                    listingDoc.data!!.get("currentOccupants")!!.toString(),
                    listingDoc.data!!.get("description")!!.toString(),
                    listingDoc.data!!.get("facilities") as List<String>,
                    listingDoc.data!!.get("listingType").toString(),
                    listingDoc.data!!.get("listingBhk")!!.toString(),
                    listingDoc.data!!.get("listingImages") as List<String>,
                    listingDoc.data!!.get("createdBy")!!.toString(),
                    listingDoc!!.id.toString())



            }
                .filterNotNull()

            result=Result.Success(listings)
        }catch (e : Exception)
        {
            Log.e(TAG, "getMyListings: ",e )
            result=Result.Failure(e.message.toString())
        }

        return result
    }
}