package com.ketworie.wheep.client.chat

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.ZonedDateTime

@Dao
interface MessageDao {

    @Query("SELECT * FROM Message WHERE hubId = :hubId ORDER BY date DESC")
    fun getRecent(hubId: String): DataSource.Factory<Int, Message>

    @Query("SELECT * FROM Message WHERE hubId = :hubId ORDER BY date DESC LIMIT 1")
    fun getLast(hubId: String): Message?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(m: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(hubs: List<Message>)

    @Query("DELETE FROM Message")
    suspend fun deleteAll()

    @Query("SELECT * FROM Message WHERE date <= :dateTime AND hubId = :hubId ORDER BY date DESC LIMIT :limit")
    fun loadInit(hubId: String, dateTime: ZonedDateTime, limit: Int): List<Message>

    @Query("SELECT * FROM Message WHERE date < :dateTime AND hubId = :hubId ORDER BY date DESC LIMIT :limit")
    fun loadPrev(hubId: String, dateTime: ZonedDateTime, limit: Int): List<Message>

    @Query("SELECT * FROM Message WHERE date > :dateTime AND hubId = :hubId ORDER BY date ASC LIMIT :limit")
    fun loadNext(hubId: String, dateTime: ZonedDateTime, limit: Int): List<Message>

}