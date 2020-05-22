package com.ketworie.wheep.client.hub.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ketworie.wheep.client.hub.HubService
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserService
import javax.inject.Inject

class HubListActivityViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var hubService: HubService

    @Inject
    lateinit var userService: UserService

    val me: LiveData<User> by lazy {
        userService.getMe()
    }

}