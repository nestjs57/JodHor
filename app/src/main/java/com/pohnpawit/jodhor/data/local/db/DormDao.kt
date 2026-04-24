package com.pohnpawit.jodhor.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pohnpawit.jodhor.data.local.entity.DormEntity
import com.pohnpawit.jodhor.data.local.entity.DormWithPhotos
import kotlinx.coroutines.flow.Flow

@Dao
interface DormDao {
    @Query("SELECT * FROM dorms ORDER BY isFull ASC, isFavorite DESC, createdAt DESC")
    fun observeAll(): Flow<List<DormEntity>>

    @Transaction
    @Query("SELECT * FROM dorms ORDER BY isFull ASC, isFavorite DESC, createdAt DESC")
    fun observeAllWithPhotos(): Flow<List<DormWithPhotos>>

    @Query("SELECT * FROM dorms WHERE id = :id")
    fun observeById(id: Long): Flow<DormEntity?>

    @Query("SELECT * FROM dorms WHERE id = :id")
    suspend fun getById(id: Long): DormEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dorm: DormEntity): Long

    @Update
    suspend fun update(dorm: DormEntity)

    @Delete
    suspend fun delete(dorm: DormEntity)

    @Query("UPDATE dorms SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE dorms SET isFull = :isFull WHERE id = :id")
    suspend fun setFull(id: Long, isFull: Boolean)

    @Query("UPDATE dorms SET status = :status, viewedAt = :viewedAt WHERE id = :id")
    suspend fun setStatus(id: Long, status: String, viewedAt: Long?)
}
