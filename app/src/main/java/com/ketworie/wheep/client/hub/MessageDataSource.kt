package com.ketworie.wheep.client.hub

import androidx.paging.ItemKeyedDataSource

class MessageDataSource : ItemKeyedDataSource<Long, Message>() {
    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Message>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Message>) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Message>) {
        TODO("Not yet implemented")
    }

    override fun getKey(item: Message): Long {
        TODO("Not yet implemented")
    }
}