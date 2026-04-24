package com.pohnpawit.jodhor.data.repository

import com.pohnpawit.jodhor.data.model.CoverPhoto
import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.model.DormPreview
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.model.PhoneContact
import com.pohnpawit.jodhor.data.model.Photo
import kotlinx.coroutines.flow.Flow

interface DormRepository {
    fun observeDormPreviews(): Flow<List<DormPreview>>
    fun observeDorm(id: Long): Flow<Dorm?>
    suspend fun getDorm(id: Long): Dorm?
    suspend fun upsertDorm(dorm: Dorm): Long
    suspend fun deleteDorm(id: Long)
    suspend fun reorderDorms(orderedIds: List<Long>)
    suspend fun setFavorite(id: Long, isFavorite: Boolean)
    suspend fun setFull(id: Long, isFull: Boolean)
    suspend fun setStatus(id: Long, status: DormStatus)

    fun observePhotos(dormId: Long): Flow<List<Photo>>
    suspend fun addPhoto(dormId: Long, filePath: String, caption: String = ""): Long
    suspend fun deletePhoto(photoId: Long)
    suspend fun reorderPhotos(orderedIds: List<Long>)

    fun observeCovers(dormId: Long): Flow<List<CoverPhoto>>
    suspend fun addCover(dormId: Long, filePath: String): Long
    suspend fun deleteCover(coverId: Long)
    suspend fun reorderCovers(orderedIds: List<Long>)

    fun observePhones(dormId: Long): Flow<List<PhoneContact>>
    suspend fun getPhones(dormId: Long): List<PhoneContact>
    suspend fun replaceDormPhones(dormId: Long, phones: List<PhoneContact>)
}
