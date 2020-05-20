package com.ketworie.wheep.client.hub

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ketworie.wheep.client.chat.ChatService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubDao @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    private var myHubs: List<Hub>? = null

    fun getMyHubs(): LiveData<List<Hub>> {
        return liveData {
            myHubs?.let {
                emit(it)
                return@liveData
            }
            myHubs = chatService.getMyHubs().also { emit(it) }
        }
    }
}