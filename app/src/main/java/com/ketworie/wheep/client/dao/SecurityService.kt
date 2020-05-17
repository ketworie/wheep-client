package com.ketworie.wheep.client.dao

import com.ketworie.wheep.client.MainApplication
import javax.inject.Inject

class SecurityService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService


    suspend fun login(login: String, password: String): String {
        val response = chatService.login(login, password)
        return response.headers().get(MainApplication.X_AUTH_TOKEN)!!
    }
}