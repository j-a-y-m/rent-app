package com.phics23.tenant.data.model.payments

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


@Entity
data class PaymentWithTransaction(
    @Embedded
    val payment: Payment,
    @Relation(
        parentColumn = "paymentId",
        entityColumn = "paymentId"
    )
    val transaction : Transaction?
)
{

}
