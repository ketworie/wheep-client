package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.DataSource
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.notebook.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            val user = withContext(Dispatchers.IO) { chatService.get(id) }
            userDao.save(user)
        }
        return userDao.get(id)
    }

    fun getMe(): LiveData<User> {
        return liveData {
            if (userId.isEmpty()) {
                val me = withContext(Dispatchers.IO) { chatService.getMe() }
                userId = me.id
                userDao.save(me)
            }
            emitSource(userDao.get(userId))
        }
    }

    fun resetUserId() {
        userId = ""
    }

    suspend fun loadContacts() {
        val contacts = chatService.listContacts().map { Contact(it) }
        if (contacts.isEmpty())
            return
        userDao.deleteContacts()
        userDao.saveContacts(contacts)
        val absentContacts = contacts.filter { userDao.existsById(it.userId) }
        val users = chatService.getList(absentContacts.map { it.userId })
        userDao.saveAll(users)
    }

    fun getContacts(): DataSource.Factory<Int, User> {
        return userDao.getContacts()
    }
}