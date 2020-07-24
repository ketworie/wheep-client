package com.ketworie.wheep.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserService
import javax.inject.Inject

class HomeActivityViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var userService: UserService

    fun getMe(): LiveData<User> {
        return userService.getMe()
    }

}