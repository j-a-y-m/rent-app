package com.phics23.tenant.data.service.listingsHome

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class listingsService @Inject constructor(val firebaseFirestore: FirebaseFirestore) {

    suspend fun getListings(city : String): Result<List<Listing>>
    {
        var result : Result<List<Listing>> = Result.Loading()
        try {
            val myListingIds = mutableListOf<String>()
            val listingDocs = firebaseFirestore.collection("Listings")
                                                        .whereEqualTo("city", city).get().await()
            if (listingDocs != null) {
                val listings = listingDocs.map { document->



                    Listing(document?.data!!.get("title")!!.toString(),
                            document.data.get("address")!!.toString(),
                            document.data.get("city")!!.toString(),
                            document.data.get("area")!!.toString(),
                            document.data.get("price")!!.toString(),
                            document.data.get("totalOccupants")!!.toString(),
                            document.data.get("currentOccupants")!!.toString(),
                            document.data.get("description")!!.toString(),
                            document.data.get("facilities") as List<String>,
                            document.data.get("listingType").toString(),
                            document.data.get("listingBhk")!!.toString(),
                            document.data.get("listingImages") as List<String>,
                            document.data["createdBy"] as String??: "null",
                            document.id.toString())
                } //.also { listing->Log.e("listingservice", "getListings: "+listing.toString() ) }    }

                result = Result.Success(listings)
            }else result=Result.Failure("no listings found")

        }catch (e : Exception)
        {
            Log.e("listingservice", "getListings: "+result,e )
            result = Result.Failure(e.message.toString()+" ; Couldn't retrive listings" )
        }

        return result
    }

    suspend fun getListing(listingId : String) : Result<Listing>
    {
        var result : Result<Listing> = Result.Loading()
        try {
            val myListingIds = mutableListOf<String>()
            val listingDoc = firebaseFirestore.collection("Listings").
                                document(listingId).get().await()


            val listing = Listing(listingDoc.data!!.get("title")!!.toString(),
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
                    listingDoc.data!!["createdBy"] as String??: "null",
                    listingDoc.id.toString())
            return Result.Success(listing)
        }catch (e : Exception)
        {
            result=Result.Failure(e.message.toString())
            return Result.Failure(e.message.toString())
        }

        return result
    }
}