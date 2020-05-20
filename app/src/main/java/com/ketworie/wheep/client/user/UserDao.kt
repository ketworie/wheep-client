package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ketworie.wheep.client.chat.ChatService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDao @Inject constructor() {
    @Inject
    lateinit var chatService: ChatService

    private var me: User? = null

    fun getMe(): LiveData<User> {
        return liveData {
            me?.let {
                emit(it)
                return@liveData
            }
            me = chatService.getMe().also { emit(it) }
        }
    }
}