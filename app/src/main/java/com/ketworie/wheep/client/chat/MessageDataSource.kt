package com.ketworie.wheep.client.chat

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import java.time.ZonedDateTime

class MessageDataSource(
    private val db: RoomDatabase,
    private val messageService: MessageService,
    private val hubId: String
) :
    ItemKeyedDataSource<ZonedDateTime, Message>() {

    init {
        db.invalidationTracker.addObserver(object : InvalidationTracker.Observer("Message") {
            override fun onInvalidated(tables: MutableSet<String>) {
                Log.d("DS", "invalidate")
                invalidate()
            }
        })
    }

    override fun isInvalid(): Boolean {
        db.invalidationTracker.refreshVersionsAsync()
        return super.isInvalid()
    }

    override fun loadInitial(
        params: LoadInitialParams<ZonedDateTime>,
        callback: LoadInitialCallback<Message>
    ) {
        val dateTime = params.requestedInitialKey ?: ZonedDateTime.now()
        val limit = params.requestedLoadSize
        // initial key can belong to upper or lower item on recycler view
        // so to avoid visual stuttering loading messages around that position
        val init = messageService.fetchInit(hubId, dateTime, limit / 2)
        val next = messageService.fetchNext(hubId, dateTime, limit / 2)
        val messages = next + init
        Log.d("DS", "Limit $limit. Initial ${messages.size} $dateTime")
        callback.onResult(messages)
    }

    override fun loadAfter(params: LoadParams<ZonedDateTime>, callback: LoadCallback<Message>) {
        val limit = params.requestedLoadSize
        val messages = messageService.fetchPrev(hubId, params.key, limit)
        Log.d("DS", "Limit $limit. Prev ${messages.size} ${params.key}")
        callback.onResult(messages)
    }

    override fun loadBefore(params: LoadParams<ZonedDateTime>, callback: LoadCallback<Message>) {
        val limit = params.requestedLoadSize
        val messages = messageService.fetchNext(hubId, params.key, limit)
        Log.d("DS", "Limit $limit. Next ${messages.size} ${params.key}")
        callback.onResult(messages)
    }

    override fun getKey(item: Message): ZonedDateTime {
        return item.date
    }
}