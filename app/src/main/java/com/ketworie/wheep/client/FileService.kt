package com.ketworie.wheep.client

import android.net.Uri
import androidx.core.net.toFile
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.network.GenericError
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    suspend fun uploadImage(image: Uri): GenericError<String> {
        val file = image.toFile()
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        return chatService.uploadImage(body)
    }
}