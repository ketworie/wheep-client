package com.ketworie.wheep.client.notebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserService
import javax.inject.Inject

class ContactListFragmentViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var userService: UserService

    fun getContacts(): LiveData<PagedList<User>> {
        return userService.getContacts()
            .toLiveData(10, boundaryCallback = ContactBoundaryCallback(userService))
    }
}