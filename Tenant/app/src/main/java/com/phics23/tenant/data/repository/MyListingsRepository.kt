package com.phics23.tenant.data.repository

import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.service.listingsHome.MyListingsServiceTemp
import com.phics23.tenant.util.Result
import javax.inject.Inject

class MyListingsRepository @Inject constructor(val myListingsService: MyListingsServiceTemp){

    suspend fun getMyListings(): Result<List<Listing>> {
        return myListingsService.getMyListings()
    }

}