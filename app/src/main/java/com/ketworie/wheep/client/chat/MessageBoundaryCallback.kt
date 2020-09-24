package com.ketworie.wheep.client.chat

import com.ketworie.wheep.client.AsyncBoundaryCallback
import java.time.ZonedDateTime

class MessageBoundaryCallback(private val messageService: MessageService, val hubId: String) :
    AsyncBoundaryCallback<Message>() {

    override suspend fun loadInitial() {
        messageService.loadPrev(hubId, ZonedDateTime.now())
    }

    override suspend fun loadNext(itemAtEnd: Message) {
        messageService.loadPrev(hubId, itemAtEnd.date)
    }

    override suspend fun loadPrevious(itemAtFront: Message) {
        messageService.loadNext(hubId, itemAtFront.date)
    }
}