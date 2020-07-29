package com.ketworie.wheep.client.hub

import com.ketworie.wheep.client.AsyncBoundaryCallback

class HubListBoundaryCallback(private val hubService: HubService) :
    AsyncBoundaryCallback<HubMessage>() {

    override suspend fun loadInitial() {
        hubService.loadInitial()
    }

    override suspend fun loadNext(itemAtEnd: HubMessage) {
        // TODO
    }

    override suspend fun loadPrevious(itemAtFront: HubMessage) {
        //TODO
    }
}