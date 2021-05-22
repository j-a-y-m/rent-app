package com.phics23.tenant.data.model.payments

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation



data class BookingWithPayments(
    @Embedded
    val booking: Booking,
    @Relation(
        parentColumn = "bookingId",
        entityColumn = "bookingId"
    )
    val payments : List<Payment>
)
{

}
