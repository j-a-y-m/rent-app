package com.phics23.tenant.data.repository

import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.service.listingsHome.MyListingDetailServiceTemp
import com.phics23.tenant.util.Result
import javax.inject.Inject

class MyListingDetailRepository @Inject constructor(val myListingDetailServiceTemp: MyListingDetailServiceTemp) {

    suspend fun getMyListing(listingid : String) : Result<Listing>
    {
       return myListingDetailServiceTemp.getMyListing(listingid)
    }
}