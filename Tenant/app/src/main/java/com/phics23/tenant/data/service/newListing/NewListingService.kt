package com.phics23.tenant.data.service.newListing

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.io.File
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class NewListingService @Inject constructor(val firebaseFirestore: FirebaseFirestore, val firebaseStorage : FirebaseStorage, val firestoreAuth: FirebaseAuth) {



    suspend fun uploadImages(imageFiles : List<File>) : Result<List<String>>
    {
        var result : Result<List<String>> = Result.Loading()
        val urlList = mutableListOf<String>()
        val folderRef = firebaseStorage.reference.child("Listings/${UUID.randomUUID()}")
        for (image in imageFiles)
        {
            try {
                var imgRef = folderRef.child("${UUID.randomUUID()}")
                imgRef.putFile(Uri.fromFile(image)).await()
                urlList.add(imgRef.downloadUrl.await().toString())
            }catch (e : Exception)
            {
                result = Result.Failure(e.message.toString())
                return result
            }

        }
        result= Result.Success(urlList.toList())
        return result
    }

    suspend fun submitListing(listing : Listing) : Result<String>
    {
        val listing = hashMapOf(
            "title" to listing.title,
            "address" to listing.address,
            "city" to listing.city,
            "area" to listing.area,
            "price" to listing.price,
            "totalOccupants" to listing.totalOccupants,
            "description" to listing.description,
            "facilities" to listing.facilities,
            "listingType" to listing.listingType,
            "listingBhk" to listing.listingBhk,
            "createdBy"  to firestoreAuth.currentUser.uid,
            "listingImages" to listing.listingImages
        )
        try {
            firebaseFirestore.collection("ListingRequests").document().set(listing).await()
            return Result.Success("Your listing will be live after approval")
        }catch (e : Exception)
        {
            Log.e("TAG", "createNewUser: executionException"+e )
            return Result.Failure(e.message?:"error submitting listing")

        }

    }

}