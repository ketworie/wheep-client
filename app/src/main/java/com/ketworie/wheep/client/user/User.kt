package com.ketworie.wheep.client.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity
data class User(
    @PrimaryKey
    val id: String,
    var alias: String,
    var name: String,
    var image: String,
    var lastModified: ZonedDateTime
)