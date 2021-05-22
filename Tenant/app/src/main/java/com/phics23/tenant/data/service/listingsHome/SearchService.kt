package com.phics23.tenant.data.service.listingsHome

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchService @Inject constructor(val firestore: FirebaseFirestore) {

//    suspend fun search(city: String): Result<List<String>> {
//        var result: Result<List<String>> = Result.Loading()
//        firestore.collection("Cities").document(city)
//
//    }

    suspend fun getCities(): Result<List<String>> {
        val functions = Firebase.functions
        var result: Result<List<String>> = Result.Loading()
        val cities = mutableListOf<String>()
//        val listingDocs = firestore.collection("Cities")
//                .whereEqualTo("city", city).get().await()


        try {

            val response = functions
                    .getHttpsCallable("getCities")
                    .call().await()

            if (response.data != null) {
               val res = response.data as HashMap<String,ArrayList<String>>
                Log.e("TAG", "getCities: "+res.toString() )
                res.get("doucumentIds")?.map { it-> Log.e("map", "getCities: "+it ) }
                result = Result.Success(res.get("doucumentIds")!!.toList())
            } else result = Result.Failure("no listings found")
        } catch (e: Exception) {
            Log.e("searchservice", "getCities: err", e)
            result = Result.Failure(e.message.toString())
        }

        return result
    }

}