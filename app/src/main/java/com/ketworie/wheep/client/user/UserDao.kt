package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE id = :id")
    fun get(id: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: User)

    @Delete
    suspend fun delete(user: User)
}