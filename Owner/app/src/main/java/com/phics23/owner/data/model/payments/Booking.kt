package com.phics23.tenant.data.model.payments

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.function.BooleanSupplier


@Entity
data class Booking(
    @PrimaryKey
    val bookingId : String,
    val listingId : String,
    val ownerId : String,
    val startDate : Long,
    val isActive : Boolean,

) {

}