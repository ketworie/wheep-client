package com.ketworie.wheep.client.chat

import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class MessageBoundaryCallback(val messagingService: MessagingService, val hubId: String) :
    PagedList.BoundaryCallback<Message>() {

    val loadNext = Mutex()
    val loadPrev = Mutex()
    val loadRecent = Mutex()

    override fun onZeroItemsLoaded() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!loadRecent.tryLock())
                return@launch
            try {
                messagingService.loadMessages(hubId)
            } finally {
                loadRecent.unlock()
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            if (!loadPrev.tryLock()) {
                return@launch
            }
            try {
                messagingService.loadPrevMessages(hubId, itemAtEnd.date)
            } finally {
                loadPrev.unlock()
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            if (!loadNext.tryLock()) {
                return@launch
            }
            try {
                messagingService.loadNextMessages(hubId, itemAtFront.date)
            } finally {
                loadNext.unlock()
            }
        }
    }
}