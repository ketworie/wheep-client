package com.ketworie.wheep.client.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ketworie.wheep.client.chat.Message
import com.ketworie.wheep.client.chat.MessageDao
import com.ketworie.wheep.client.hub.Hub
import com.ketworie.wheep.client.hub.HubDao
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserDao

@TypeConverters(Converters::class)
@Database(entities = [User::class, Hub::class, Message::class], version = 2)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun hubDao(): HubDao

    abstract fun messageDao(): MessageDao
}