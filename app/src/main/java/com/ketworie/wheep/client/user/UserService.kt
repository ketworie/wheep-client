package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ketworie.wheep.client.dao.ChatService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor() {
    @Inject
    lateinit var chatService: ChatService

    private var me: User? = null

    fun getMe(): LiveData<User> {
        return liveData {
            if (me != null)
                me?.let { emit(it) }
            me = chatService.getMe()
            me?.let { emit(it) }
        }
    }
}