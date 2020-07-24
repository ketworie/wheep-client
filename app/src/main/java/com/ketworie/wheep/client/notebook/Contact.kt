package com.ketworie.wheep.client.notebook

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey
    val userId: String
)