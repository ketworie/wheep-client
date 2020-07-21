package com.ketworie.wheep.client.hub

import androidx.room.Embedded
import com.ketworie.wheep.client.chat.Message

data class HubMessage(
    @Embedded
    val hub: Hub,
    @Embedded(prefix = "m_")
    val message: Message?
)