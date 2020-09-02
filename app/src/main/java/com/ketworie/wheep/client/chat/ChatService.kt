package com.ketworie.wheep.client.chat

import com.ketworie.wheep.client.hub.Hub
import com.ketworie.wheep.client.hub.HubAdd
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.user.User
import okhttp3.MultipartBody
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

    @GET("/me")
    suspend fun getMe(): GenericError<User>

    @GET("/user")
    suspend fun get(@Query("id") id: String): GenericError<User>

    @GET("/user")
    suspend fun getByAlias(@Query("alias") alias: String): GenericError<User>

    @POST("/user/list")
    suspend fun getList(@Body idList: List<String>): List<User>

    @GET("/contact/list")
    suspend fun listContacts(): List<String>

    @GET("/contact/add")
    suspend fun addContact(@Query("id") id: String): GenericError<Unit>

    @GET("/contact/remove")
    suspend fun removeContact(@Query("id") id: String): GenericError<Unit>

    @GET("/me/hubs")
    suspend fun getMyHubs(): List<Hub>

    @GET("/hub/prev")
    suspend fun getPreviousMessages(
        @Query("hub") hubId: String,
        @Query("date") date: ZonedDateTime
    ): GenericError<List<Message>>

    @POST("/hub")
    suspend fun addHub(@Body add: HubAdd): GenericError<Hub>

    @POST("/hub/send")
    suspend fun sendMessage(@Body message: MessageSend): Message

    @GET("/hub/next")
    suspend fun getNextMessages(
        @Query("hub") hubId: String,
        @Query("date") date: ZonedDateTime
    ): GenericError<List<Message>>

    @Multipart
    @POST("/upload/image")
    suspend fun uploadImage(@Part image: MultipartBody.Part): GenericError<String>

    @Multipart
    @POST("/avatar/update")
    suspend fun updateAvatar(@Part image: MultipartBody.Part): GenericError<String>

}