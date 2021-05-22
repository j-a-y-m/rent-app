package com.phics23.owner.data.model.listing

import java.io.File

data class _Listing(val title: String,
                    val address: String,
                    val city: String,
                    val area: String,
                    val price: String,
                    val totalOccupants: String,
                    val description: String,
                    val facilities: List<String>,
                    val listingType: String,
                    val listingBhk: String,
                    val listingImages: List<File>
                   ) {
}