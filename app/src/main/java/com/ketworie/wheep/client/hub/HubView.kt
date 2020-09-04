package com.ketworie.wheep.client.hub

import com.ketworie.wheep.client.user.UserHub
import java.time.ZonedDateTime

data class HubView(
    val id: String,
    val name: String,
    val image: String,
    val isDialog: Boolean?,
    val lastModified: ZonedDateTime,
    val users: List<String>
) {
    fun toEntity(): Pair<Hub, List<UserHub>> {
        return Pair(
            Hub(id, name, image, isDialog, lastModified),
            users.map { UserHub(it, id) }
        )
    }
}