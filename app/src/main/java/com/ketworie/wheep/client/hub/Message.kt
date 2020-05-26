package com.ketworie.wheep.client.hub

import androidx.room.Entity
import java.time.ZonedDateTime

@Entity
data class Message(
    val id: String,
    val userId: String,
    val hubId: String,
    val subId: Int,
    val text: String,
    val time: ZonedDateTime
)