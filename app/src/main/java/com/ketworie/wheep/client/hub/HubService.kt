package com.ketworie.wheep.client.hub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.ketworie.wheep.client.chat.ChatService
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

    fun get(id: String): LiveData<Hub> {
        return hubDao.get(id)
    }

    suspend fun refresh() {
        try {
            val myHubs = chatService.getMyHubs()
            hubDao.deleteAll()
            hubDao.saveList(myHubs)
        } catch (e: Exception) {
            Log.e("HubService", "Error during hub refresh", e)
        }
    }
}