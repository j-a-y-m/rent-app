package com.phics23.owner.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tenant(var tenantId : String,
                  var email: String,
                  val name: String,
                  var address: String,
                  var phoneNumber: String,
                  @Nullable
                  var displayImageUrl : String?,
                  val bookingId : String,
                  val listingId : String,
                  val ownerId : String,
                  val startDate : Long,
                  val isActive : Boolean,
                  val isDue : Boolean

) : Parcelable
{
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readLong(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tenantId)
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(phoneNumber)
        parcel.writeString(displayImageUrl)
        parcel.writeString(bookingId)
        parcel.writeString(listingId)
        parcel.writeString(ownerId)
        parcel.writeLong(startDate)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeByte(if (isDue) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tenant> {
        override fun createFromParcel(parcel: Parcel): Tenant {
            return Tenant(parcel)
        }

        override fun newArray(size: Int): Array<Tenant?> {
            return arrayOfNulls(size)
        }
    }

}