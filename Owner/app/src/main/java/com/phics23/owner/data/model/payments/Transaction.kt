package com.phics23.tenant.data.model.payments

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*


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