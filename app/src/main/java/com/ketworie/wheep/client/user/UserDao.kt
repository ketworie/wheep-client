package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE id = :id")
    fun get(id: String): LiveData<User>

    @Query("SELECT u.* FROM User u JOIN Contact c ON c.userId = u.id ORDER BY u.name")
    fun getContacts(): DataSource.Factory<Int, User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: User)

    @Delete
    suspend fun delete(user: User)
}