package com.ketworie.wheep.client.hub

data class Hub(
    val id: String,
    var name: String,
    var image: String,
    var userCount: Int?,
    var lastMessage: Message?
)
