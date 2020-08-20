package com.ketworie.wheep.client.user

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.ketworie.wheep.client.contact.Contact

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE id = :id")
    fun get(id: String): LiveData<User>

    @Query("SELECT u.* FROM User u JOIN Contact c ON c.userId = u.id ORDER BY u.name")
    fun getContacts(): DataSource.Factory<Int, User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(user: List<User>)

    @Delete
    suspend fun delete(user: User)

    @Query("UPDATE User SET image = :image WHERE id = :id")
    suspend fun updateAvatar(id: String, image: String)

    @Query("SELECT EXISTS(SELECT * FROM User WHERE id = :id)")
    suspend fun existsById(id: String): Boolean

    @Query("DELETE FROM Contact")
    suspend fun deleteContacts()

    @Query("DELETE FROM Contact WHERE userId = :userId")
    suspend fun deleteContact(userId: String)

    @Insert
    suspend fun saveContacts(contacts: List<Contact>)

    @Insert
    suspend fun saveContact(contact: Contact)

    @Query("SELECT EXISTS(SELECT * FROM Contact WHERE userId = :userId)")
    fun isContact(userId: String): LiveData<Boolean>

}