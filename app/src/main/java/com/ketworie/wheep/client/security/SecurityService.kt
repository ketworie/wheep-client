package com.ketworie.wheep.client.security

import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.chat.ChatService
import javax.inject.Inject

class SecurityService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService


    suspend fun login(login: String, password: String): String {
        val response = try {
            chatService.login(login, password)
        } catch (e: Exception) {
            throw RuntimeException("Network error")
        }
        if (!response.isSuccessful)
            throw RuntimeException("Wrong login or password")
        return response.headers().get(MainApplication.X_AUTH_TOKEN)!!
    }
}