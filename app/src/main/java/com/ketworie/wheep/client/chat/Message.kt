package com.ketworie.wheep.client.chat

import androidx.room.Entity
import java.time.ZonedDateTime

@Entity
data class Message(
    val id: String,
    val userId: String,
    val hubId: String,
    val nextId: String,
    val text: String,
    val date: ZonedDateTime
)