package com.phics23.owner.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phics23.owner.data.model.owner.Owner


@Database(entities = arrayOf(Owner::class),version = 3,exportSchema = false)
abstract class OwnerDatabase : RoomDatabase() {

    abstract fun ownerDao(): OwnerDao
}