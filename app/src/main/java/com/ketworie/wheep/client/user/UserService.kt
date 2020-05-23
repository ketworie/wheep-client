package com.ketworie.wheep.client.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ketworie.wheep.client.chat.ChatService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                Log.i("Service", "Before getMe")
                val me = withContext(Dispatchers.IO) { chatService.getMe() }
                Log.i("Service", "After getMe")
                userId = me.id
                userDao.save(me)
            }
            emitSource(userDao.get(userId))
        }
    }
}