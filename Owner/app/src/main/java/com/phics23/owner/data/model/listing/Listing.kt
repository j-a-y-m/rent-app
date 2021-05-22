package com.phics23.owner.data.model.listing

import android.os.Parcel
import android.os.Parcelable

data class Listing(val title: String,
                   val address: String,
                   val city: String,
                   val area: String,
                   val price: String,
                   val totalOccupants: String,
                   val currentOccupants: String,
                   val description: String,
                   val facilities: List<String>,
                   val listingType: String,
                   val listingBhk: String,
                   val listingImages: List<String>,
                   val createdBy: String,
                   val id:String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.createStringArrayList()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.createStringArrayList()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(area)
        parcel.writeString(price)
        parcel.writeString(totalOccupants)
        parcel.writeString(currentOccupants)
        parcel.writeString(description)
        parcel.writeStringList(facilities)
        parcel.writeString(listingType)
        parcel.writeString(listingBhk)
        parcel.writeStringList(listingImages)
        parcel.writeString(createdBy)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Listing> {
        override fun createFromParcel(parcel: Parcel): Listing {
            return Listing(parcel)
        }

        override fun newArray(size: Int): Array<Listing?> {
            return arrayOfNulls(size)
        }
    }

}