package com.ketworie.wheep.client.hub

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Hub(
    @PrimaryKey
    val id: String,
    var name: String,
    var image: String,
    var isDialog: Boolean?
)
