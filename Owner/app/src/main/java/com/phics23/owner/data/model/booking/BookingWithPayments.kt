package com.phics23.owner.data.model.booking

import androidx.room.Embedded
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
