package com.pohnpawit.jodhor.data.repository

import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.model.DormPreview
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.model.Photo
import kotlinx.coroutines.flow.Flow

interface DormRepository {
    fun observeDormPreviews(): Flow<List<DormPreview>>
    fun observeDorm(id: Long): Flow<Dorm?>
    suspend fun getDorm(id: Long): Dorm?
    suspend fun upsertDorm(dorm: Dorm): Long
    suspend fun deleteDorm(id: Long)
    suspend fun setFavorite(id: Long, isFavorite: Boolean)
    suspend fun setStatus(id: Long, status: DormStatus)

    fun observePhotos(dormId: Long): Flow<List<Photo>>
    suspend fun addPhoto(dormId: Long, filePath: String, caption: String = ""): Long
    suspend fun deletePhoto(photoId: Long)
    suspend fun reorderPhotos(orderedIds: List<Long>)
}
