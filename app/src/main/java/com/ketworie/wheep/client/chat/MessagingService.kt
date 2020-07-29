package com.ketworie.wheep.client.chat

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagingService @Inject constructor() {

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    lateinit var chatService: ChatService

    fun getPaged(hubId: String): LiveData<PagedList<Message>> {
        return messageDao.getRecent(hubId)
            .toLiveData(10, boundaryCallback = MessageBoundaryCallback(this, hubId))
    }

    fun getLast(hubId: String): Message? {
        return messageDao.getLast(hubId)
    }

    suspend fun loadMessages(hubId: String) {
        val messages = chatService.getPreviousMessages(hubId, ZonedDateTime.now())
        messageDao.saveAll(messages)
    }

    suspend fun loadNextMessages(hubId: String, dateTime: ZonedDateTime) {
        val messages = chatService.getNextMessages(hubId, dateTime)
        messageDao.saveAll(messages)
    }

    suspend fun loadPrevMessages(hubId: String, dateTime: ZonedDateTime) {
        val messages = chatService.getPreviousMessages(hubId, dateTime)
        messageDao.saveAll(messages)
    }

    suspend fun sendMessage(message: MessageSend) {
        val sentMessage = chatService.sendMessage(message)
        messageDao.save(sentMessage)
    }

}