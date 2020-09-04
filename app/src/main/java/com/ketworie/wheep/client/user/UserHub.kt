package com.ketworie.wheep.client.user

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["userId", "hubId"], indices = [Index("hubId")])
data class UserHub(
    val userId: String,
    val hubId: String
)