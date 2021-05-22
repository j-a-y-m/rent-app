package com.phics23.tenant.data.model.tenant

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tenant(var email: String,
                  val name: String,
                  var address: String,
                  var phoneNumber: String,
                  @Nullable
                  var displayImageUrl : String?
)
{

    @PrimaryKey(autoGenerate = true)
    var id : Int = 1
}