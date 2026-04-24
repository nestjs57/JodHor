package com.pohnpawit.jodhor.data.model

data class Dorm(
    val id: Long = 0,
    val name: String,
    val address: String = "",
    val priceMonthly: Int? = null,
    val securityDeposit: Int? = null,
    val advancePayment: Int? = null,
    val contractYears: Int? = null,
    val mapUrl: String = "",
    val notes: String = "",
    val status: DormStatus = DormStatus.NOT_CONTACTED,
    val isFavorite: Boolean = false,
    val isFull: Boolean = false,
    val sortOrder: Int = 0,
    val coverPhotoId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val viewedAt: Long? = null,
)
