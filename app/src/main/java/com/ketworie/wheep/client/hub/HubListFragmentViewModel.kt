package com.ketworie.wheep.client.hub

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ketworie.wheep.client.contact.Contact
import com.ketworie.wheep.client.user.UserService
import javax.inject.Inject

class HubListFragmentViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var hubService: HubService

    @Inject
    lateinit var userService: UserService

    fun getHubs(): LiveData<PagedList<HubMessage>> {
        return hubService.getRecent()
            .toLiveData(10, boundaryCallback = HubListBoundaryCallback(hubService))
    }

    fun getContacts(): LiveData<List<Contact>> {
        return userService.getContacts()
    }
}