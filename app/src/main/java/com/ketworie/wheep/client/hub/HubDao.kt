package com.ketworie.wheep.client.hub

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface HubDao {

    @Query(
        "SELECT Hub.*, " +
                "Message.userId AS m_userId, Message.text AS m_text, Message.id as m_id, Message.hubId AS m_hubId, Message.prevId as m_prevId, Message.date AS m_date " +
                "FROM Hub " +
                "LEFT JOIN Message ON (Message.hubId = Hub.id AND Message.date = (SELECT MAX(m.date) FROM Message m)) " +
                "ORDER BY Message.date DESC"
    )
    fun getRecent(): DataSource.Factory<Int, HubMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(hub: Hub)

    @Query("SELECT * FROM Hub WHERE id = :id")
    fun get(id: String): LiveData<Hub>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(hubs: List<Hub>)

    @Delete
    suspend fun delete(hub: Hub)

    @Query("DELETE FROM Hub")
    suspend fun deleteAll()
}