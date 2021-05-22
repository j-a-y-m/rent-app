package com.phics23.owner.data.repository

import com.phics23.owner.data.model.listing.Listing
import com.phics23.owner.data.service.newListing.NewListingService
import com.phics23.owner.util.Result
import java.io.File
import javax.inject.Inject

class NewListingRepository @Inject constructor(val newListingService: NewListingService)
{

    suspend fun submitListing(listing: Listing): Result<String> {
        return newListingService.submitListing(listing)
    }

    suspend fun uploadImages( imageFiles : List<File>): Result<List<String>> {
        return  newListingService.uploadImages(imageFiles)
    }

}