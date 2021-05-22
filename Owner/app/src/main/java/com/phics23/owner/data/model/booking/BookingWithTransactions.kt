package com.phics23.owner.data.model.booking

import androidx.room.Embedded
import androidx.room.Relation



data class BookingWithTransactions(
    @Embedded
    val booking: Booking,
    @Relation(
        parentColumn = "bookingId",
        entityColumn = "bookingId"
    )
    val transactions : List<Transaction>
)
{

}
