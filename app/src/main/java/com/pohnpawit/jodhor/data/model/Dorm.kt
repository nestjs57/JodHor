package com.pohnpawit.jodhor.data.model

data class Dorm(
    val id: Long = 0,
    val name: String,
    val address: String = "",
    val priceMonthly: Int? = null,
    val contactPhone: String = "",
    val mapUrl: String = "",
    val notes: String = "",
    val status: DormStatus = DormStatus.PLANNED,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val viewedAt: Long? = null,
)
