package com.ketworie.wheep.client.chat

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDao {

    @Query("SELECT * FROM Message WHERE hubId = :hubId ORDER BY date DESC")
    fun getRecent(hubId: String): DataSource.Factory<Int, Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(m: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveList(hubs: List<Message>)

    @Query("DELETE FROM Message")
    suspend fun deleteAll()

}