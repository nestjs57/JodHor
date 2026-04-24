package com.pohnpawit.jodhor.data.model

data class Photo(
    val id: Long = 0,
    val dormId: Long,
    val filePath: String,
    val caption: String = "",
    val takenAt: Long = System.currentTimeMillis(),
)
