package com.pohnpawit.jodhor.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pohnpawit.jodhor.data.model.PhoneContact

@Entity(
    tableName = "phone_numbers",
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
data class PhoneContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dormId: Long,
    val number: String,
    val note: String,
)

fun PhoneContactEntity.toDomain() = PhoneContact(
    id = id,
    dormId = dormId,
    number = number,
    note = note,
)

fun PhoneContact.toEntity() = PhoneContactEntity(
    id = id,
    dormId = dormId,
    number = number,
    note = note,
)
