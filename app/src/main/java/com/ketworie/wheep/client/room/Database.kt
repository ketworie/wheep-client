package com.ketworie.wheep.client.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ketworie.wheep.client.chat.Message
import com.ketworie.wheep.client.chat.MessageDao
import com.ketworie.wheep.client.contact.Contact
import com.ketworie.wheep.client.hub.Hub
import com.ketworie.wheep.client.hub.HubDao
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserDao
import com.ketworie.wheep.client.user.UserHub

@TypeConverters(Converters::class)
@Database(
    entities = [User::class, Hub::class, Message::class, Contact::class, UserHub::class],
    version = 8,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun hubDao(): HubDao

    abstract fun messageDao(): MessageDao
}