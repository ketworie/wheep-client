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
    lateinit var messagingService: MessagingService

    fun getHub(id: String): LiveData<HubWithUsers> {
        return hubService.get(id)
    }

    fun getMessages(hubId: String): LiveData<PagedList<Message>> {
        return messagingService.getPaged(hubId)
    }

    suspend fun sendMessage(message: MessageSend) {
        messagingService.sendMessage(message)
    }
}
