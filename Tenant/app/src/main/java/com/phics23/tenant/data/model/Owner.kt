package com.phics23.tenant.data.model

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Owner(var ownerId : String,
                 val name: String,
                 val email: String,
                 var address: String,
                 var phoneNumber: String,
                 @Nullable
                    var displayImageUrl : String?
)
{

    @PrimaryKey
    var id : Int = 0
}