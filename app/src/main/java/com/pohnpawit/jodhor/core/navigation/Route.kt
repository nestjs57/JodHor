package com.pohnpawit.jodhor.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object DormList : Route

    @Serializable
    data class DormDetail(val dormId: Long) : Route

    @Serializable
    data class AddDorm(val dormId: Long? = null) : Route

    @Serializable
    data class PhotoViewer(val dormId: Long, val photoId: Long) : Route
}
