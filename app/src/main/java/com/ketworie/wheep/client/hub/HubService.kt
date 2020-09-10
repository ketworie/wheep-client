package com.ketworie.wheep.client.hub

import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.network.NetworkResponse
import com.ketworie.wheep.client.user.UserDao
import com.ketworie.wheep.client.user.UserHub
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    @Inject
    lateinit var hubDao: HubDao

    @Inject
    lateinit var userDao: UserDao

    fun getRecent(): DataSource.Factory<Int, HubMessage> {
        return hubDao.getRecent()
    }

    fun get(id: String): LiveData<Hub> {
        return hubDao.get(id)
    }

    fun getWithUsers(id: String): LiveData<HubWithUsers> {
        return hubDao.getWithUsers(id)
    }

    suspend fun deleteAll() {
        hubDao.deleteAll()
    }

    suspend fun loadInitial() {
        val myHubs = chatService.getMyHubs()
        val pairs = myHubs.map(HubView::toEntity)
        hubDao.saveAll(pairs.map(Pair<Hub, List<UserHub>>::first))
        userDao.saveUserHubs(pairs.flatMap(Pair<Hub, List<UserHub>>::second))
    }

    suspend fun add(add: HubAdd): GenericError<HubView> {
        val response = chatService.addHub(add)
        if (response is NetworkResponse.Success) {
            val pair = response.body.toEntity()
            hubDao.save(pair.first)
            userDao.saveUserHubs(pair.second)
        }
        return response
    }

    suspend fun updateAvatar(id: String, image: Uri): GenericError<String> {
        val file = image.toFile()
        val idBody = RequestBody.create(MediaType.parse("text/plain"), id)
        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val updateAvatar = chatService.updateHubAvatar(idBody, body)
        if (updateAvatar is NetworkResponse.Success) {
            hubDao.updateAvatar(id, updateAvatar.body)
        }
        return updateAvatar
    }

    suspend fun rename(id: String, name: String): GenericError<Unit> {
        val response = chatService.renameHub(id, name)
        if (response is NetworkResponse.Success)
            hubDao.updateName(id, name)
        return response
    }

    suspend fun addUsers(hubId: String, userIds: List<String>): GenericError<Unit> {
        val response = chatService.addUsers(hubId, userIds)
        if (response is NetworkResponse.Success)
            userDao.saveUserHubs(userIds.map { UserHub(it, hubId) })
        return response
    }

    suspend fun removeUser(hubId: String, userId: String): GenericError<Unit> {
        val response = chatService.removeUser(hubId, userId)
        if (response is NetworkResponse.Success)
            userDao.deleteUserHub(UserHub(userId, hubId))
        return response
    }

}