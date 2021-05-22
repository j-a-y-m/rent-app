package com.phics23.tenant.data.service.listingsHome

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class MyListingDetailServiceTemp @Inject constructor(val firebaseFirestore: FirebaseFirestore, val firebaseAuth: FirebaseAuth)  {

    private val uid = firebaseAuth.currentUser!!.uid
    suspend fun getMyListing(listingid : String) : Result<Listing>
    {   var result : Result<Listing> = Result.Loading()

        try {
            val myListingIds = mutableListOf<String>()
            val list = mutableListOf<Listing>()
            val mylisting = firebaseFirestore.collection("ListingRequests").document(listingid).get().await()

               val listing =  Listing(mylisting?.data!!.get("title")!!.toString(),
                                mylisting.data!!.get("address")!!.toString(),
                                mylisting.data!!.get("city")!!.toString(),
                                mylisting.data!!.get("area")!!.toString(),
                                mylisting.data!!.get("price")!!.toString(),
                                mylisting.data!!.get("totalOccupants")!!.toString(),
                                mylisting.data!!.get("currentOccupants")!!.toString(),
                                mylisting.data!!.get("description")!!.toString(),
                                mylisting.data!!.get("facilities") as List<String>,
                                mylisting.data!!.get("listingType").toString(),
                                mylisting.data!!.get("listingBhk")!!.toString(),
                                mylisting.data!!.get("listingImages") as List<String>,
                       mylisting.data!!.get("createdBy") as String  ,mylisting.id.toString()  ).also { it-> Log.e("list", "getMyListings: "+it.toString() ) }

            result= Result.Success(listing)
        }catch (e : Exception)
        {
            Log.e("TAG", "getMyListings: "+e.message.toString() )
            result= Result.Failure(e.message.toString())
        }

        return result
    }





}