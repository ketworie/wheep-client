package com.ketworie.wheep.client.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserService
import javax.inject.Inject

class ContactAddActivityViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var userService: UserService

    fun search(alias: String): LiveData<GenericError<User>> {
        return userService.getByAlias(alias)
    }
}