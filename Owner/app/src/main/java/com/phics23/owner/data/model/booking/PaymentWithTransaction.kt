package com.phics23.owner.data.model.booking

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
