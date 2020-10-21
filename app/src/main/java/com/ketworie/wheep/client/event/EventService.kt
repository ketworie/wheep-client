package com.ketworie.wheep.client.event

import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.chat.Message
import com.ketworie.wheep.client.chat.MessageDao
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.network.NetworkResponse
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    @Inject
    lateinit var messageDao: MessageDao


    suspend fun setupQueue(): GenericError<Unit> {
        return chatService.setupQueue()
    }

    suspend fun save(event: Event) {
        when (val body = event.body) {
            is Message -> messageDao.save(body)
        }
    }

    suspend fun checkLast(from: ZonedDateTime): Event? {
        val response = chatService.getLastEvent(from)
        if (response !is NetworkResponse.Success) {
            return null
        }
        val event = response.body
        save(event)
        return event
    }


}