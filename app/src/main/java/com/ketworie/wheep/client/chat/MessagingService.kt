package com.ketworie.wheep.client.chat

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ketworie.wheep.client.MainApplication.Companion.SERVER_BASE
import com.ketworie.wheep.client.hub.Message
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagingService @Inject constructor() {

    @Inject
    lateinit var client: OkHttpClient
    private val objectMapper = jacksonObjectMapper()

    fun subscribe(hubId: String, reader: (Message) -> Unit): WebSocket {
        val request = Request.Builder().url(SERVER_BASE + "/chat").get().build()
        return client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val message = objectMapper.readValue(text, Message::class.java)
                    reader.invoke(message)
                } catch (e: Exception) {
                    Log.e("Messaging", "Error during message receive", e)
                }
            }
        })
    }

    fun WebSocket.write(message: Message): Boolean {
        val writeValueAsString = objectMapper.writeValueAsString(message)
        return send(writeValueAsString)
    }
}