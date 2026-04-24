package com.pohnpawit.jodhor.data.model

enum class DormStatus {
    NOT_CONTACTED,
    CONTACTED,
    VIEWED,
}

fun DormStatus.next(): DormStatus = when (this) {
    DormStatus.NOT_CONTACTED -> DormStatus.CONTACTED
    DormStatus.CONTACTED -> DormStatus.VIEWED
    DormStatus.VIEWED -> DormStatus.NOT_CONTACTED
}
