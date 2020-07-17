package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.security.AuthInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor() {
    @Inject
    lateinit var authInterceptor: AuthInterceptor

    @Inject
    lateinit var chatService: ChatService

    @Inject
    lateinit var userDao: UserDao

    lateinit var userId: String
    var lastToken: String? = null

    fun getUser(id: String): LiveData<User> {
        CoroutineScope(Dispatchers.IO).launch {
            val user = withContext(Dispatchers.IO) { chatService.get(id) }
            userDao.save(user)
        }
        return userDao.get(id)
    }

    fun getMe(): LiveData<User> {
        return liveData {
            if (!::userId.isInitialized || lastToken != authInterceptor.token) {
                val me = withContext(Dispatchers.IO) { chatService.getMe() }
                userId = me.id
                lastToken = authInterceptor.token
                userDao.save(me)
            }
            emitSource(userDao.get(userId))
        }
    }
}