package com.ketworie.wheep.client.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity
data class Message(
    @PrimaryKey
    val id: String,
    val userId: String,
    val hubId: String,
    val prevId: String,
    val text: String,
    val date: ZonedDateTime
)