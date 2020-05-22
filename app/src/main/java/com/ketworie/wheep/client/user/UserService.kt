package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ketworie.wheep.client.chat.ChatService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor() {
    @Inject
    lateinit var chatService: ChatService

    @Inject
    lateinit var userDao: UserDao
    lateinit var userId: String

    fun getMe(): LiveData<User> {
        return liveData {
            if (!::userId.isInitialized) {
                val me = chatService.getMe()
                userId = me.id
                userDao.insertOrUpdate(me)
            }
            emitSource(userDao.get(userId))
        }
    }
}