package com.ketworie.wheep.client.chat

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.network.NetworkResponse
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageService @Inject constructor() {

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    lateinit var chatService: ChatService

    fun getPaged(hubId: String): LiveData<PagedList<Message>> {
        return messageDao.getRecent(hubId)
            .toLiveData(10, boundaryCallback = MessageBoundaryCallback(this, hubId))
    }

    suspend fun loadNext(hubId: String, dateTime: ZonedDateTime) {
        val messages = chatService.getNextMessages(hubId, dateTime)
        if (messages is NetworkResponse.Success)
            messageDao.saveAll(messages.body)
    }

    suspend fun loadPrev(hubId: String, dateTime: ZonedDateTime) {
        val messages = chatService.getPreviousMessages(hubId, dateTime)
        if (messages is NetworkResponse.Success)
            messageDao.saveAll(messages.body)
    }

    suspend fun send(message: MessageSend) {
        val sentMessage = chatService.sendMessage(message)
        messageDao.save(sentMessage)
    }

    suspend fun save(message: Message) {
        messageDao.save(message)
    }

    suspend fun setupQueue(): GenericError<Unit> {
        return chatService.setupQueue()
    }

}