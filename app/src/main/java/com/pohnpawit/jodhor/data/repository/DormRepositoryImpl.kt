package com.pohnpawit.jodhor.data.repository

import com.pohnpawit.jodhor.data.local.db.DormDao
import com.pohnpawit.jodhor.data.local.db.PhotoDao
import com.pohnpawit.jodhor.data.local.entity.PhotoEntity
import com.pohnpawit.jodhor.data.local.entity.toDomain
import com.pohnpawit.jodhor.data.local.entity.toEntity
import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.model.DormPreview
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.model.Photo
import com.pohnpawit.jodhor.data.storage.PhotoFileStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DormRepositoryImpl @Inject constructor(
    private val dormDao: DormDao,
    private val photoDao: PhotoDao,
    private val photoFileStore: PhotoFileStore,
) : DormRepository {

    override fun observeDormPreviews(): Flow<List<DormPreview>> =
        dormDao.observeAllWithPhotos().map { list -> list.map { it.toDomain() } }

    override fun observeDorm(id: Long): Flow<Dorm?> =
        dormDao.observeById(id).map { it?.toDomain() }

    override suspend fun getDorm(id: Long): Dorm? = dormDao.getById(id)?.toDomain()

    override suspend fun upsertDorm(dorm: Dorm): Long =
        if (dorm.id == 0L) {
            dormDao.insert(dorm.toEntity())
        } else {
            dormDao.update(dorm.toEntity())
            dorm.id
        }

    override suspend fun deleteDorm(id: Long) {
        val entity = dormDao.getById(id) ?: return
        dormDao.delete(entity)
    }

    override suspend fun setFavorite(id: Long, isFavorite: Boolean) {
        dormDao.setFavorite(id, isFavorite)
    }

    override suspend fun setStatus(id: Long, status: DormStatus) {
        val viewedAt = if (status == DormStatus.VIEWED) System.currentTimeMillis() else null
        dormDao.setStatus(id, status.name, viewedAt)
    }

    override fun observePhotos(dormId: Long): Flow<List<Photo>> =
        photoDao.observeByDorm(dormId).map { list -> list.map { it.toDomain() } }

    override suspend fun addPhoto(dormId: Long, filePath: String, caption: String): Long {
        val nextOrder = photoDao.maxSortOrder(dormId) + 1
        return photoDao.insert(
            PhotoEntity(
                dormId = dormId,
                filePath = filePath,
                caption = caption,
                takenAt = System.currentTimeMillis(),
                sortOrder = nextOrder,
            )
        )
    }

    override suspend fun deletePhoto(photoId: Long) {
        val photo = photoDao.getById(photoId) ?: return
        photoDao.delete(photo)
        photoFileStore.delete(photo.filePath)
    }

    override suspend fun reorderPhotos(orderedIds: List<Long>) {
        photoDao.applyOrder(orderedIds)
    }
}
