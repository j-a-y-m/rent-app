package com.phics23.tenant.data.service.listingsHome

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class listingsServiceTemp @Inject constructor(val firebaseFirestore: FirebaseFirestore) {

    suspend fun getListings(city : String): Result<List<Listing>>
    {
        var result : Result<List<Listing>> = Result.Loading()
//        try {
//            val myListingIds = mutableListOf<String>()
//            val listingDocRef = firebaseFirestore.collection("ListingRequests")
//                                                        .whereEqualTo("city", city).get().await()

//            val listings = listingDocRef.map { document->
////                 Listing(document?.data!!.get("title")!!.toString(),
////                    document.data!!.get("address")!!.toString(),
////                    document.data.get("city")!!.toString(),
////                    document.data.get("area")!!.toString(),
////                    document.data.get("price")!!.toString(),
////                    document.data.get("totalOccupants")!!.toString(),
////                    document.data.get("description")!!.toString(),
////                    document.data.get("facilities") as List<String>,
////                    document.data.get("listingType").toString(),
////                    document.data.get("listingBhk")!!.toString(),
////                    document.data.get("listingImages") as List<String>,
////                         document.data.get("createdBy") as String) }
//
//            return Result.Success(listings)
//        }catch (e : Exception)
//        {
//            result=Result.Failure(e.message.toString())
//        }

        return result
    }

//    suspend fun getListings(queryParameter : String, value : String): Result<List<Listing>>
//    {
//        var result : Result<List<Listing>> = Result.Loading()
//        try {
//            val myListingIds = mutableListOf<String>()
//            val listingDocRef = firebaseFirestore.collection("ListingRequests")
//                .whereEqualTo("city", city).get().await()
//
//            val listings = listingDocRef.map { document->
//                document.toObject<Listing>() }
//
//            return Result.Success(listings)
//        }catch (e : Exception)
//        {
//            result=Result.Failure(e.message.toString())
//        }
//
//        return result
//    }
}