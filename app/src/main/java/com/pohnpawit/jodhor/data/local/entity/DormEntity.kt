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
    val contactPhone: String,
    val mapUrl: String,
    val notes: String,
    val status: DormStatus,
    val isFavorite: Boolean,
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
    contactPhone = contactPhone,
    mapUrl = mapUrl,
    notes = notes,
    status = status,
    isFavorite = isFavorite,
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
    contactPhone = contactPhone,
    mapUrl = mapUrl,
    notes = notes,
    status = status,
    isFavorite = isFavorite,
    createdAt = createdAt,
    viewedAt = viewedAt,
)
