package com.phics23.tenant.data.model.payments

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.util.TableInfo
import java.time.LocalDate

//used as a token type thing during payment containing which month duration
//this trx is done for
//when one is processed and data entered to trx another is created and entered in dues
//while paying dues this obj is used to do trx
@Entity
data class Payment(

    @PrimaryKey(autoGenerate = false)
    var paymentId : String ,
    val pcStartDate : Long,
    val pcEndDate : Long,
    val amount : String,
    val isDue : Boolean,
    val isPaid : Boolean,
    @Nullable
    val transactionId : String?,
    @Nullable
    val paidDate : Long?,
    val bookingId : String,

) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString()!!,
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(paymentId)
        parcel.writeLong(pcStartDate)
        parcel.writeLong(pcEndDate)
        parcel.writeString(amount)
        parcel.writeByte(if (isDue) 1 else 0)
        parcel.writeByte(if (isPaid) 1 else 0)
        parcel.writeString(transactionId)
        parcel.writeValue(paidDate)
        parcel.writeString(bookingId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Payment> {
        override fun createFromParcel(parcel: Parcel): Payment {
            return Payment(parcel)
        }

        override fun newArray(size: Int): Array<Payment?> {
            return arrayOfNulls(size)
        }
    }

}
