package com.ketworie.wheep.client.chat

import com.ketworie.wheep.client.hub.Hub
import com.ketworie.wheep.client.user.User
import retrofit2.Response
import retrofit2.http.*
import java.time.ZonedDateTime

interface ChatService {

    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): Response<Unit>

    @GET("/user/me")
    suspend fun getMe(): User

    @GET("/user")
    suspend fun get(@Query("id") id: String): User

    @GET("/user/me/hubs")
    suspend fun getMyHubs(): List<Hub>

    @GET("/hub/prev")
    suspend fun getPreviousMessages(
        @Query("hub") hubId: String,
        @Query("date") date: ZonedDateTime
    ): List<Message>

    @POST("/hub/send")
    suspend fun sendMessage(@Body message: MessageSend): Message

    @GET("/hub/next")
    suspend fun getNextMessages(
        @Query("hub") hubId: String,
        @Query("date") date: ZonedDateTime
    ): List<Message>

}