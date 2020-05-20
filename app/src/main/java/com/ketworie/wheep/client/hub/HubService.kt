package com.ketworie.wheep.client.hub

import com.ketworie.wheep.client.user.User
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubService @Inject constructor() {

    suspend fun getMyHubs(): List<Hub> {
        val hubs = List(10) { i -> Hub(i.toString(), "Hub number $i", null) }
        hubs.forEach {
            it.lastMessage = Message(
                it.id,
                User("", "pp", "bb", ""),
                it,
                "Turboslavs against reptilians. Turboslavs against reptilians. Turboslavs against reptilians. ",
                ZonedDateTime.now()
            )
        }
        return hubs
    }
}