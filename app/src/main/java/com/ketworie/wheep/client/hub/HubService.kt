package com.ketworie.wheep.client.hub

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.network.NetworkResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    @Inject
    lateinit var hubDao: HubDao

    fun getRecent(): DataSource.Factory<Int, HubMessage> {
        return hubDao.getRecent()
    }

    fun get(id: String): LiveData<Hub> {
        return hubDao.get(id)
    }

    suspend fun deleteAll() {
        hubDao.deleteAll()
    }

    suspend fun loadInitial() {
        val myHubs = chatService.getMyHubs()
        hubDao.saveAll(myHubs)
    }

    suspend fun addHub(add: HubAdd): GenericError<Hub> {
        val response = chatService.addHub(add)
        if (response is NetworkResponse.Success)
            hubDao.save(response.body)
        return response
    }

}