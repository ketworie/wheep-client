package com.ketworie.wheep.client.hub

data class HubAdd(
    var name: String,
    var image: String,
    var users: List<String>
)