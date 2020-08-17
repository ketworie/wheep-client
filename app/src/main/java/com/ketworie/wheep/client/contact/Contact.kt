package com.ketworie.wheep.client.contact

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey
    val userId: String
)