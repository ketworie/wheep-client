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
    val nextId: String,
    val text: String,
    val date: ZonedDateTime
)

data class MessageSend(
    val hubId: String,
    val text: String
)

fun List<Message>.isConsistent(): Boolean {
    var nextId: String? = null
    for (message in this) {
        val currentId = message.id
        if (nextId != null && !isDefault(nextId) && !isDefault(currentId) && nextId == currentId)
            return false
        nextId = currentId
    }
    return true
}

private fun isDefault(id: String): Boolean {
    return id == "000000000000000000000000"
}