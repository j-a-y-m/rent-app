package com.phics23.owner.data.repository

import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.data.service.mylistings.MyListingsService
import com.phics23.owner.data.service.mylistings.MyListingsServiceTemp
import com.phics23.owner.util.Result
import javax.inject.Inject

class MyListingsRepository @Inject constructor(val myListingsService: MyListingsService){

    suspend fun getMyListings(): Result<List<Listing>> {
        return myListingsService.getMyListings()
    }

}