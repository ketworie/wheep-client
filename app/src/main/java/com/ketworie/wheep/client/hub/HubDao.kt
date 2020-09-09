package com.ketworie.wheep.client.hub

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface HubDao {

    @Query(
        "SELECT Hub.*, " +
                "Message.userId AS m_userId, " +
                "Message.text AS m_text, " +
                "Message.id as m_id, " +
                "Message.hubId AS m_hubId, " +
                "Message.prevId as m_prevId, " +
                "Message.date AS m_date " +
                "FROM Hub " +
                "LEFT JOIN Message ON Message.id = (SELECT t.id FROM " +
                "(SELECT m.id FROM Message m WHERE m.hubId = Hub.id ORDER BY m.date DESC) t LIMIT 1) " +
                "ORDER BY COALESCE(Message.date, Hub.lastModified) DESC"
    )
    fun getRecent(): DataSource.Factory<Int, HubMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(hub: Hub)

    @Query("SELECT * FROM Hub WHERE id = :id")
    fun get(id: String): LiveData<Hub>

    @Transaction
    @Query("SELECT * FROM Hub WHERE id = :id")
    fun getWithUsers(id: String): LiveData<HubWithUsers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(hubs: List<Hub>)

    @Delete
    suspend fun delete(hub: Hub)

    @Query("DELETE FROM Hub")
    suspend fun deleteAll()

    @Query("UPDATE Hub SET image = :image WHERE id = :id")
    suspend fun updateAvatar(id: String, image: String)
}