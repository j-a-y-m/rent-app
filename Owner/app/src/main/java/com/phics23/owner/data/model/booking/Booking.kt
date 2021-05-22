package com.phics23.owner.data.model.booking

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Booking(
    @PrimaryKey
    val bookingId : String,
    val listingId : String,
    val ownerId : String,
    val tenantId : String,
    val startDate : Long,
    val isActive : Boolean,
    val isDue : Boolean
) {

}