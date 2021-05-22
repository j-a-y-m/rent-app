package com.phics23.tenant.data.room

import androidx.room.*
import com.phics23.tenant.data.model.tenant.Tenant


@Dao
interface TenantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTenant(tenant : Tenant)

    @Update
    suspend fun updateTenant(tenant:  Tenant)

    @Delete
    suspend fun  deleteTenant(tenant:  Tenant)

    @Query("select * from Tenant ORDER BY id DESC LIMIT 1")
    suspend fun getTenant() :  Tenant

    @Query("select COUNT(*) from Tenant")
    suspend fun tenantCount() :  Int
}