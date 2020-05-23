package com.ketworie.wheep.client.hub

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface HubDao {

    @Query("SELECT * FROM Hub ORDER BY date DESC")
    fun getRecent(): DataSource.Factory<Int, Hub>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(hub: Hub)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveList(hubs: List<Hub>)

    @Delete
    suspend fun delete(hub: Hub)

    @Query("DELETE FROM Hub")
    suspend fun deleteAll()
}