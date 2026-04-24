package com.pohnpawit.jodhor.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pohnpawit.jodhor.data.model.Photo

@Entity(
    tableName = "photos",
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
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dormId: Long,
    val filePath: String,
    val caption: String,
    val takenAt: Long,
)

fun PhotoEntity.toDomain() = Photo(
    id = id,
    dormId = dormId,
    filePath = filePath,
    caption = caption,
    takenAt = takenAt,
)

fun Photo.toEntity() = PhotoEntity(
    id = id,
    dormId = dormId,
    filePath = filePath,
    caption = caption,
    takenAt = takenAt,
)
