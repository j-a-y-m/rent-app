package com.phics23.owner.data.service.mylistings

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class MyListingsServiceTemp @Inject constructor(val firebaseFirestore: FirebaseFirestore, val firebaseAuth: FirebaseAuth) {

//    private val uid = firebaseAuth.currentUser!!.uid
//    suspend fun getMyListings() : Result<List<Listing>>
//    {   var result : Result<List<Listing>> = Result.Loading()
//
//        try {
//            val myListingIds = mutableListOf<String>()
////            val mylistingsId = firebaseFirestore.collection("Owners")
////                .document(uid).collection("listings").get().await()
////            for(listingId in mylistingsId)
////            {
////                myListingIds.add(listingId.id)
////            }
//            val list = mutableListOf<Listing>()
//            val mylistings = firebaseFirestore.collection("ListingRequests").whereEqualTo("createdBy",uid).get().await()
////            val myListingsDocumentRef=myListingIds.map {id->
////                firebaseFirestore.collection("Listings").document(id)  }
//            for(listing in mylistings)
//            {
//                list.add(
//                    Listing(listing?.data!!.get("title")!!.toString(),
//                                  listing.data!!.get("address")!!.toString(),
//                                    listing.data!!.get("city")!!.toString(),
//                        listing.data!!.get("area")!!.toString(),
//                        listing.data!!.get("price")!!.toString(),
//                        listing.data!!.get("totalOccupants")!!.toString(),
//                        listing.data!!.get("description")!!.toString(),
//                        listing.data!!.get("facilities") as List<String>,
//                        listing.data!!.get("listingType").toString(),
//                        listing.data!!.get("listingBhk")!!.toString(),
//                        listing.data!!.get("listingImages") as List<String> ,""   ).also { it->Log.e("list", "getMyListings: "+it.toString() ) }
//
//                )
//
//            }
////            val listings = myListingsDocumentRef.map { listingDocRef->
////                listingDocRef.get().await()
////            }.map { listingDocSnapshot->listingDocSnapshot.toObject<_Listing>() }
////                .filterNotNull()
//
//            result=Result.Success(list)
//        }catch (e : Exception)
//        {
//            Log.e("TAG", "getMyListings: e"+e.message.toString() )
//            result=Result.Failure(e.message.toString())
//        }
//
//        return result
//    }
}