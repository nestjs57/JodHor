package com.pohnpawit.jodhor.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.pohnpawit.jodhor.data.local.entity.CoverPhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoverPhotoDao {
    @Query("SELECT * FROM cover_photos WHERE dormId = :dormId ORDER BY sortOrder ASC, id ASC")
    fun observeByDorm(dormId: Long): Flow<List<CoverPhotoEntity>>

    @Query("SELECT * FROM cover_photos WHERE id = :id")
    suspend fun getById(id: Long): CoverPhotoEntity?

    @Query("SELECT COALESCE(MAX(sortOrder), -1) FROM cover_photos WHERE dormId = :dormId")
    suspend fun maxSortOrder(dormId: Long): Int

    @Insert
    suspend fun insert(cover: CoverPhotoEntity): Long

    @Delete
    suspend fun delete(cover: CoverPhotoEntity)

    @Query("UPDATE cover_photos SET sortOrder = :order WHERE id = :id")
    suspend fun updateSortOrder(id: Long, order: Int)

    @Transaction
    suspend fun applyOrder(orderedIds: List<Long>) {
        orderedIds.forEachIndexed { index, id -> updateSortOrder(id, index) }
    }
}
