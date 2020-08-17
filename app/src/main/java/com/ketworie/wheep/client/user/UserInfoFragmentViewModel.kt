package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class UserInfoFragmentViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var userService: UserService

    fun isContact(userId: String): LiveData<Boolean> {
        return userService.isContact(userId)
    }
}