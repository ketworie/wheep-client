package com.ketworie.wheep.client.chat;

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ketworie.wheep.client.hub.Hub
import com.ketworie.wheep.client.hub.HubService
import javax.inject.Inject;

class ChatActivityViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var hubService: HubService

    fun getHub(id: String): LiveData<Hub> {
        return hubService.get(id)
    }
}
