package com.ketworie.wheep.client.chat

import com.ketworie.wheep.client.hub.Hub
import com.ketworie.wheep.client.user.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatService {

    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): Response<Unit>

    @GET("/user/me")
    suspend fun getMe(): User

    @GET("/user/me/hubs")
    suspend fun getMyHubs(): List<Hub>

}