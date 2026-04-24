package com.pohnpawit.jodhor.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.pohnpawit.jodhor.data.model.DormPreview

data class DormWithPhotos(
    @Embedded val dorm: DormEntity,
    @Relation(parentColumn = "id", entityColumn = "dormId")
    val photos: List<PhotoEntity>,
)

fun DormWithPhotos.toDomain(): DormPreview {
    val sorted = photos.sortedBy { it.sortOrder }.map { it.toDomain() }
    val coverId = dorm.coverPhotoId
    val cover = if (coverId != null) {
        sorted.firstOrNull { it.id == coverId } ?: sorted.firstOrNull()
    } else {
        sorted.firstOrNull()
    }
    return DormPreview(
        dorm = dorm.toDomain(),
        photos = sorted,
        cover = cover,
    )
}
