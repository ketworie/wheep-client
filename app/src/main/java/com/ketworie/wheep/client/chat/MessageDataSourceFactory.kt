package com.ketworie.wheep.client.chat

import androidx.room.RoomDatabase
import java.time.ZonedDateTime

class MessageDataSourceFactory(
    private val db: RoomDatabase,
    private val messageService: MessageService,
    private val hubId: String
) : androidx.paging.DataSource.Factory<ZonedDateTime, Message>() {
    override fun create(): androidx.paging.DataSource<ZonedDateTime, Message> {
        return MessageDataSource(db, messageService, hubId)
    }
}