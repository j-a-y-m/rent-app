package com.phics23.owner.data.room

import androidx.room.*
import com.phics23.owner.data.model.owner.Owner


@Dao
interface OwnerDao {

    @Insert
    suspend fun insertOwner(owner : Owner)

    @Update
    suspend fun updateOwner(owner:  Owner)

    @Delete
    suspend fun  deleteOwner(owner:  Owner)

    @Query("select * from Owner where ID = 1")
    suspend fun getOwner() :  Owner

    @Query("select COUNT(*) from Owner")
    suspend fun ownerCount() :  Int
}