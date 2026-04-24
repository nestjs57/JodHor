package com.pohnpawit.jodhor.data.model

data class PhoneContact(
    val id: Long = 0,
    val dormId: Long = 0,
    val number: String,
    val note: String = "",
)
