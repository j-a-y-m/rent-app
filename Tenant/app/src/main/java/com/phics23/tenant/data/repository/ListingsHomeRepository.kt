package com.phics23.tenant.data.repository

import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.service.listingsHome.listingsService
import com.phics23.tenant.data.service.listingsHome.listingsServiceTemp
import com.phics23.tenant.util.Result
import javax.inject.Inject

class ListingsHomeRepository @Inject constructor(val listingsService : listingsService) {


    suspend fun getListings(city : String): Result<List<Listing>>
    {
        return listingsService.getListings(city)
    }
}