package com.ketworie.wheep.client.dao

import com.ketworie.wheep.client.MainApplication
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
            throw RuntimeException("Login failed")
        return response.headers().get(MainApplication.X_AUTH_TOKEN)!!
    }
}