package com.ketworie.wheep.client.hub.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ketworie.wheep.client.chat.MessagingService
import com.ketworie.wheep.client.hub.HubMessage
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
    lateinit var messagingService: MessagingService

    @Inject
    lateinit var userService: UserService

    fun getMe(): LiveData<User> {
        return userService.getMe()
    }

    fun getHubs(): LiveData<PagedList<HubMessage>> {
        return hubService.getRecent().toLiveData(10)
    }

    fun refreshHubs() {
        viewModelScope.launch(Dispatchers.IO) {
            hubService.refresh()
        }
    }


}