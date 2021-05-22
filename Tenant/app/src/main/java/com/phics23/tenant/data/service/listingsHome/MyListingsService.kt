package com.phics23.tenant.data.service.listingsHome

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class MyListingsService @Inject constructor(val firebaseFirestore: FirebaseFirestore, val firebaseAuth: FirebaseAuth) {

    private val uid = firebaseAuth.currentUser!!.uid
    suspend fun getMyListings() : Result<List<Listing>>
    {   var result : Result<List<Listing>> = Result.Loading()

        try {
            val myListingIds = mutableListOf<String>()
            val mylistingsId = firebaseFirestore.collection("Owners")
                .document(uid).collection("listings").get().await()
            for(listingId in mylistingsId)
            {
                myListingIds.add(listingId.id)
            }

            val myListingsDocumentRef=myListingIds.map {id->
                firebaseFirestore.collection("Listings").document(id)  }

            val listings = myListingsDocumentRef.map { listingDocRef->
                listingDocRef.get().await()
            }.map { listingDocSnapshot->listingDocSnapshot.toObject<Listing>() }
                .filterNotNull()
            result=Result.Success(listings)
        }catch (e : Exception)
        {
            result=Result.Failure(e.message.toString())
        }

        return result
    }
}