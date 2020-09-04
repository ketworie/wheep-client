package com.ketworie.wheep.client.hub

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.network.NetworkResponse
import com.ketworie.wheep.client.user.UserDao
import com.ketworie.wheep.client.user.UserHub
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    @Inject
    lateinit var hubDao: HubDao

    @Inject
    lateinit var userDao: UserDao

    fun getRecent(): DataSource.Factory<Int, HubMessage> {
        return hubDao.getRecent()
    }

    fun get(id: String): LiveData<HubWithUsers> {
        return hubDao.get(id)
    }

    suspend fun deleteAll() {
        hubDao.deleteAll()
    }

    suspend fun loadInitial() {
        val myHubs = chatService.getMyHubs()
        val pairs = myHubs.map(HubView::toEntity)
        hubDao.saveAll(pairs.map(Pair<Hub, List<UserHub>>::first))
        userDao.saveUserHubs(pairs.flatMap(Pair<Hub, List<UserHub>>::second))
    }

    suspend fun addHub(add: HubAdd): GenericError<HubView> {
        val response = chatService.addHub(add)
        if (response is NetworkResponse.Success) {
            val pair = response.body.toEntity()
            hubDao.save(pair.first)
            userDao.saveUserHubs(pair.second)
        }
        return response
    }

}