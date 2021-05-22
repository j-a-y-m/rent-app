package com.phics23.owner.data.model.owner

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Owner(var email: String,
                    val name: String,
                    var address: String,
                    var phoneNumber: String,
                    @Nullable
                    var displayImageUrl : String?
)
{

    @PrimaryKey(autoGenerate = false)
    var id : Int = 1
}