package com.ketworie.wheep.client.hub

import com.ketworie.wheep.client.user.UserHub

data class HubView(
    val id: String,
    val name: String,
    val image: String,
    val isDialog: Boolean?,
    val users: List<String>
) {
    fun toEntity(): Pair<Hub, List<UserHub>> {
        return Pair(
            Hub(id, name, image, isDialog),
            users.map { UserHub(it, id) }
        )
    }
}