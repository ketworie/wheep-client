package com.ketworie.wheep.client.room

import androidx.room.TypeConverter
import java.time.ZonedDateTime

class Converters {

    @TypeConverter
    fun encodeZonedDateTime(time: ZonedDateTime): String {
        return time.toString()
    }

    @TypeConverter
    fun decodeZonedDateTime(time: String): ZonedDateTime {
        return ZonedDateTime.parse(time)
    }
}