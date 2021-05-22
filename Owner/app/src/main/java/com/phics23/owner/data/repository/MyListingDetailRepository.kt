package com.phics23.owner.data.repository

import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.data.service.mylistings.MyListingDetailServiceTemp
import com.phics23.owner.util.Result
import javax.inject.Inject

class MyListingDetailRepository @Inject constructor(val myListingDetailServiceTemp: MyListingDetailServiceTemp) {

    suspend fun getMyListing(listingid : String) : Result<Listing>
    {
       return myListingDetailServiceTemp.getMyListing(listingid)
    }
}