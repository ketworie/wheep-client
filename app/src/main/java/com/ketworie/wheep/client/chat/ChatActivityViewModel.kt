package com.ketworie.wheep.client.chat;

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.ketworie.wheep.client.hub.HubService
import com.ketworie.wheep.client.hub.HubWithUsers
import javax.inject.Inject

class ChatActivityViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var hubService: HubService

    @Inject
    lateinit var messageService: MessageService

    fun getHub(id: String): LiveData<HubWithUsers> {
        return hubService.getWithUsers(id)
    }

    fun getMessages(hubId: String): LiveData<PagedList<Message>> {
        return messageService.getPaged(hubId)
    }

    suspend fun sendMessage(message: MessageSend) {
        messageService.send(message)
    }
}
