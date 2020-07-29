package com.ketworie.wheep.client.hub

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity
data class Hub(
    @PrimaryKey
    val id: String,
    var name: String,
    var image: String,
    var userCount: Int,
    var isDialog: Boolean?,
    var lastModified: ZonedDateTime
)
