package com.ketworie.wheep.client.hub

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import java.time.ZonedDateTime

@Dao
interface HubDao {

    @Query("SELECT * FROM Hub ORDER BY lastUpdate DESC")
    fun getRecent(): DataSource.Factory<Int, Hub>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(hub: Hub)

    @Query("SELECT * FROM Hub WHERE id = :id")
    fun get(id: String) : LiveData<Hub>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveList(hubs: List<Hub>)

    @Delete
    suspend fun delete(hub: Hub)

    @Query("UPDATE Hub SET lastUpdate = :lastUpdate WHERE id = :id")
    suspend fun updateDate(id: String, lastUpdate: ZonedDateTime)

    @Query("DELETE FROM Hub")
    suspend fun deleteAll()
}