package com.ketworie.wheep.client

import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

abstract class AsyncBoundaryCallback<T> : PagedList.BoundaryCallback<T>() {

    private val loadNext = Mutex()
    private val loadPrev = Mutex()
    private val loadRecent = Mutex()

    override fun onZeroItemsLoaded() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!loadRecent.tryLock())
                return@launch
            try {
                loadInitial()
            } finally {
                loadRecent.unlock()
            }
        }
    }

    abstract suspend fun loadInitial()

    override fun onItemAtEndLoaded(itemAtEnd: T) {
        CoroutineScope(Dispatchers.IO).launch {
            if (!loadPrev.tryLock()) {
                return@launch
            }
            try {
                loadNext(itemAtEnd)
            } finally {
                loadPrev.unlock()
            }
        }
    }

    abstract suspend fun loadNext(itemAtEnd: T)

    override fun onItemAtFrontLoaded(itemAtFront: T) {
        CoroutineScope(Dispatchers.IO).launch {
            if (!loadNext.tryLock()) {
                return@launch
            }
            try {
                loadPrevious(itemAtFront)
            } finally {
                loadNext.unlock()
            }
        }
    }

    abstract suspend fun loadPrevious(itemAtFront: T)
}