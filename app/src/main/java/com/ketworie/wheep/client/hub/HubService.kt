package com.ketworie.wheep.client.hub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ketworie.wheep.client.chat.ChatService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    val myHubs: LiveData<List<Hub>> by lazy {
        liveData {
            Log.i("text", "hubs performed")
            emit(chatService.getMyHubs())
        }
    }
}