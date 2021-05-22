package com.phics23.tenant.data.room.booking

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(localDate: LocalDate?): Long? {
        return if (localDate == null) null else localDate.toEpochDay()
    }
}