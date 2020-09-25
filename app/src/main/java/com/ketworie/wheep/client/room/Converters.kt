package com.ketworie.wheep.client.room

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class Converters {

    @TypeConverter
    fun decodeZonedDateTime(value: Long?): ZonedDateTime? {
        return value?.let { ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.of("UTC")) }
    }

    @TypeConverter
    fun encodeZonedDateTime(date: ZonedDateTime?): Long? {
        return date?.toInstant()?.toEpochMilli()
    }
}