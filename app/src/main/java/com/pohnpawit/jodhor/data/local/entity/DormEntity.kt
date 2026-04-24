package com.pohnpawit.jodhor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.model.DormStatus

@Entity(tableName = "dorms")
data class DormEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val address: String,
    val priceMonthly: Int?,
    val securityDeposit: Int?,
    val advancePayment: Int?,
    val contractYears: Int?,
    val mapUrl: String,
    val notes: String,
    val status: DormStatus,
    val isFavorite: Boolean,
    val isFull: Boolean,
    val sortOrder: Int,
    val coverPhotoId: Long?,
    val createdAt: Long,
    val viewedAt: Long?,
)

fun DormEntity.toDomain() = Dorm(
    id = id,
    name = name,
    address = address,
    priceMonthly = priceMonthly,
    securityDeposit = securityDeposit,
    advancePayment = advancePayment,
    contractYears = contractYears,
    mapUrl = mapUrl,
    notes = notes,
    status = status,
    isFavorite = isFavorite,
    isFull = isFull,
    sortOrder = sortOrder,
    coverPhotoId = coverPhotoId,
    createdAt = createdAt,
    viewedAt = viewedAt,
)

fun Dorm.toEntity() = DormEntity(
    id = id,
    name = name,
    address = address,
    priceMonthly = priceMonthly,
    securityDeposit = securityDeposit,
    advancePayment = advancePayment,
    contractYears = contractYears,
    mapUrl = mapUrl,
    notes = notes,
    status = status,
    isFavorite = isFavorite,
    isFull = isFull,
    sortOrder = sortOrder,
    coverPhotoId = coverPhotoId,
    createdAt = createdAt,
    viewedAt = viewedAt,
)
