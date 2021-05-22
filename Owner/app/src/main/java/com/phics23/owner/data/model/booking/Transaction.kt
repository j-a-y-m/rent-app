package com.phics23.owner.data.model.booking

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Transaction(
    @PrimaryKey
    val transactionId : String,
    val transactionDate: Long,
    val amount  : String,
    val pcStartDate : Long,
    val pcEndDate : Long,
    val paymentId : String,
    val bookingId : String,
) {
}