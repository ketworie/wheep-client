package com.ketworie.wheep.client.hub

import androidx.paging.DataSource
import com.ketworie.wheep.client.chat.ChatService
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    @Inject
    lateinit var hubDao: HubDao

    fun getRecent(): DataSource.Factory<Int, Hub> {
        return hubDao.getRecent()
    }

    fun getMyHubs(): Call<List<Hub>> {
        return chatService.getMyHubs()
    }

    fun saveListBlocking(hubs: List<Hub>) = runBlocking {
        hubDao.saveList(hubs)
    }
}