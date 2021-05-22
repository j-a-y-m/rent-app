package com.phics23.tenant.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phics23.tenant.data.room.TenantDao
import com.phics23.tenant.data.model.tenant.Tenant


@Database(entities = arrayOf(Tenant::class),version = 5,exportSchema = false)
abstract class TenantDatabase : RoomDatabase() {

    abstract fun tenantDao(): TenantDao
}