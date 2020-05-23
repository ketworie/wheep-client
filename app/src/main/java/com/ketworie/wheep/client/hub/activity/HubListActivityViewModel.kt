package com.ketworie.wheep.client.hub.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ketworie.wheep.client.hub.Hub
import com.ketworie.wheep.client.hub.HubService
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HubListActivityViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var hubService: HubService

    @Inject
    lateinit var userService: UserService

    val me: LiveData<User> by lazy {
        userService.getMe()
    }

    val hubs: LiveData<PagedList<Hub>> by lazy {
        hubService.getRecent().toLiveData(10)
    }

    fun refreshHubs() {
        viewModelScope.launch(Dispatchers.IO) {
            hubService.refresh()
        }
    }


}