package com.ketworie.wheep.client.hub

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Hub(
    @PrimaryKey
    val id: String,
    var name: String,
    var image: String,
    var userCount: Int?,
    var isDialog: Boolean?,
    @Embedded
    var lastMessage: HubMessage?
)
