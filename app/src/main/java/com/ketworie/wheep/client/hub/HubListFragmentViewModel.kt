package com.ketworie.wheep.client.hub

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import javax.inject.Inject

class HubListFragmentViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var hubService: HubService

    fun getHubs(): LiveData<PagedList<HubMessage>> {
        return hubService.getRecent()
            .toLiveData(10, boundaryCallback = HubListBoundaryCallback(hubService))
    }
}