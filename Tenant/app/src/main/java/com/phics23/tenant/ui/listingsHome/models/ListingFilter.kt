package com.phics23.tenant.ui.listingsHome.models

data class ListingFilter(
    val priceLow : Int,
    val priceHow: Int,
    //val facilities : Facilities,
    val area : Int,
    val maxOccupants : Int,
    val BHK : Int
)
