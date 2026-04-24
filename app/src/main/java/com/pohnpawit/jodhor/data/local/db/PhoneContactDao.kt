package com.pohnpawit.jodhor.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.pohnpawit.jodhor.data.local.entity.PhoneContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhoneContactDao {
    @Query("SELECT * FROM phone_numbers WHERE dormId = :dormId ORDER BY id ASC")
    fun observeByDorm(dormId: Long): Flow<List<PhoneContactEntity>>

    @Query("SELECT * FROM phone_numbers WHERE dormId = :dormId ORDER BY id ASC")
    suspend fun getByDorm(dormId: Long): List<PhoneContactEntity>

    @Insert
    suspend fun insert(phone: PhoneContactEntity): Long

    @Query("DELETE FROM phone_numbers WHERE dormId = :dormId")
    suspend fun deleteAllForDorm(dormId: Long)

    @Transaction
    suspend fun replaceForDorm(dormId: Long, phones: List<PhoneContactEntity>) {
        deleteAllForDorm(dormId)
        phones.forEach { insert(it.copy(id = 0, dormId = dormId)) }
    }
}
