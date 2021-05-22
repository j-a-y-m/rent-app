package com.phics23.tenant.data.room.booking

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phics23.tenant.data.model.payments.Booking
import com.phics23.tenant.data.model.payments.BookingWithTransactions
import com.phics23.tenant.data.model.payments.Payment
import com.phics23.tenant.data.model.payments.Transaction

@Database(entities = arrayOf(Booking::class,
    Transaction::class,
    Payment::class,
   ),
    version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BookingDatabase : RoomDatabase() {

    abstract fun bookingDao() : BookingDao
}