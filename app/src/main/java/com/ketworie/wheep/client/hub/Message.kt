package com.ketworie.wheep.client.hub

import com.ketworie.wheep.client.user.User
import java.time.ZonedDateTime

data class Message(
    val id: String,
    val user: User,
    val hub: Hub,
    val text: String,
    val time: ZonedDateTime
) {
}