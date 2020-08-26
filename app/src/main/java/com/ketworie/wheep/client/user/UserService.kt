package com.ketworie.wheep.client.user

import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.DataSource
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.contact.Contact
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.network.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserService @Inject constructor() {

    @Inject
    lateinit var chatService: ChatService

    @Inject
    lateinit var userDao: UserDao

    var userId = ""

    fun getUser(id: String): LiveData<User> {
        CoroutineScope(Dispatchers.IO).launch {
            val response = withContext(Dispatchers.IO) { chatService.get(id) }
            if (response is NetworkResponse.Success)
                userDao.save(response.body)
        }
        return userDao.get(id)
    }

    fun getUsers(ids: List<String>): DataSource.Factory<Int, User> {
        return userDao.getAll(ids)
    }

    fun getMe(): LiveData<User> {
        return userDao.get(userId)
    }

    suspend fun loadMe(): GenericError<User> {
        val me = chatService.getMe()
        if (me is NetworkResponse.Success)
            userDao.save(me.body)
        return me
    }

    fun resetUserId() {
        userId = ""
    }

    suspend fun loadContacts() {
        val contacts = chatService.listContacts().map {
            Contact(
                it
            )
        }
        if (contacts.isEmpty())
            return
        userDao.deleteContacts()
        userDao.saveContacts(contacts)
        val absentContacts = contacts.filter { !userDao.existsById(it.userId) }
        val users = chatService.getList(absentContacts.map { it.userId })
        userDao.saveAll(users)
    }

    suspend fun removeContact(user: User): GenericError<Unit> {
        val response = chatService.removeContact(user.id)
        if (response is NetworkResponse.Success)
            userDao.deleteContact(user.id)
        return response
    }

    suspend fun addContact(user: User): GenericError<Unit> {
        val response = chatService.addContact(user.id)
        if (response is NetworkResponse.Success) {
            userDao.saveContact(Contact(user.id))
            userDao.save(user)
        }
        return response
    }

    fun getContactUsers(): DataSource.Factory<Int, User> {
        return userDao.getContactUsers()
    }

    fun getContacts(): LiveData<List<Contact>> {
        return userDao.getContacts()
    }

    fun isContact(userId: String): LiveData<Boolean> {
        return userDao.isContact(userId)
    }

    fun getByAlias(alias: String): LiveData<GenericError<User>> {
        return liveData {
            emit(chatService.getByAlias(alias))
        }
    }

    suspend fun updateAvatar(id: String, image: Uri): GenericError<String> {
        val file = image.toFile()
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        val updateAvatar = chatService.updateAvatar(body)
        if (updateAvatar is NetworkResponse.Success) {
            userDao.updateAvatar(id, updateAvatar.body)
        }
        return updateAvatar
    }
}