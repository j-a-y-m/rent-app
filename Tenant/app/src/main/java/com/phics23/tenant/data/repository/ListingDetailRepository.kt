package com.phics23.tenant.data.repository

import com.phics23.tenant.data.model.Owner
import com.phics23.tenant.data.model.listing.Listing
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.service.listingDetail.ListingDetailService
import javax.inject.Inject
import com.phics23.tenant.util.Result

class ListingDetailRepository @Inject constructor(val listingDetailService: ListingDetailService) {

    private var currentListingId : String? = null
    private lateinit var listing: Listing
    private var reviews : List<ArrayList<String>>? = null


    suspend fun getReviews(listingId: String): List<ArrayList<String>>? {
        if (currentListingId==null || listingId!=currentListingId)
        {


            when (val result = listingDetailService.getReviews(listingId)) {
                is Result.Success -> {
                    reviews = result.data

                }
                is Result.Failure -> {
                    reviews = null

                }
            }
            currentListingId=listingId
            return reviews
        }else
        {
            return reviews
        }
    }

    suspend fun getAvgRating(listingId: String) : Float?
    {
        if(reviews!=null)
        {
            val ratings = reviews!!.map { review-> return review[0].toFloat() } as List<Float>
            var sum = 0f
            val size = ratings.size
            for (rating in ratings)
            {

                sum = sum + rating

            }
            return sum/size
        }else
        return null
    }


    suspend fun getOwner(ownerId : String): Result<Owner>
    {
        var result : Result<Owner> = Result.Loading()

        when (val res = listingDetailService.getOwner(ownerId)) {
            is Result.Success -> {
                result = Result.Success(res.data)
            }
            is Result.Failure -> {
                result = Result.Failure("Error retriving owner information ")
            }
        }
        return result
    }

    fun setListing(listing: Listing) {
        this.listing=listing
    }

    fun getListing(): Listing {
        return this.listing
    }

    suspend fun contactOwner(owner: Owner) {
        val token = listingDetailService.getOwnerFcmToken(owner.ownerId)
        if (token != null) {
            listingDetailService.notifyContact(token)
        }

    }


}