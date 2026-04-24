package com.pohnpawit.jodhor.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pohnpawit.jodhor.data.model.CoverPhoto

@Entity(
    tableName = "cover_photos",
    foreignKeys = [
        ForeignKey(
            entity = DormEntity::class,
            parentColumns = ["id"],
            childColumns = ["dormId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("dormId")],
)
data class CoverPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dormId: Long,
    val filePath: String,
    val sortOrder: Int,
)

fun CoverPhotoEntity.toDomain() = CoverPhoto(
    id = id,
    dormId = dormId,
    filePath = filePath,
    sortOrder = sortOrder,
)

fun CoverPhoto.toEntity() = CoverPhotoEntity(
    id = id,
    dormId = dormId,
    filePath = filePath,
    sortOrder = sortOrder,
)
