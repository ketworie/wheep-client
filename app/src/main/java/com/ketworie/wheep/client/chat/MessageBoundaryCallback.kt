package com.ketworie.wheep.client.chat

import com.ketworie.wheep.client.AsyncBoundaryCallback

class MessageBoundaryCallback(private val messagingService: MessagingService, val hubId: String) :
    AsyncBoundaryCallback<Message>() {

    override suspend fun loadInitial() {
        messagingService.loadMessages(hubId)
    }

    override suspend fun loadNext(itemAtEnd: Message) {
        messagingService.loadPrevMessages(hubId, itemAtEnd.date)
    }

    override suspend fun loadPrevious(itemAtFront: Message) {
        messagingService.loadNextMessages(hubId, itemAtFront.date)
    }
}