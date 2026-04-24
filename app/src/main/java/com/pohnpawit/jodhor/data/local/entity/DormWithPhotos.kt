package com.pohnpawit.jodhor.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.pohnpawit.jodhor.data.model.DormPreview

data class DormWithPhotos(
    @Embedded val dorm: DormEntity,
    @Relation(parentColumn = "id", entityColumn = "dormId")
    val photos: List<PhotoEntity>,
)

fun DormWithPhotos.toDomain(): DormPreview = DormPreview(
    dorm = dorm.toDomain(),
    photos = photos.sortedBy { it.sortOrder }.map { it.toDomain() },
)
