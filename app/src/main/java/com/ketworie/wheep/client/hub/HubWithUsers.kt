package com.ketworie.wheep.client.hub

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserHub

data class HubWithUsers(
    @Embedded
    val hub: Hub,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            parentColumn = "hubId",
            entityColumn = "userId",
            value = UserHub::class
        )
    )
    val users: List<User>
)