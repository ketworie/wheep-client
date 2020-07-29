package com.ketworie.wheep.client.notebook

import com.ketworie.wheep.client.AsyncBoundaryCallback
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserService

class ContactBoundaryCallback(private val userService: UserService) :
    AsyncBoundaryCallback<User>() {

    override suspend fun loadInitial() {
        userService.loadContacts()
    }

    override suspend fun loadNext(itemAtEnd: User) {
        //TODO
    }

    override suspend fun loadPrevious(itemAtFront: User) {
        //TODO
    }
}