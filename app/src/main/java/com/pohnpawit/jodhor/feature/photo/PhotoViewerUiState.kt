package com.pohnpawit.jodhor.feature.photo

import com.pohnpawit.jodhor.data.model.Photo

data class PhotoViewerUiState(
    val isLoading: Boolean = true,
    val photos: List<Photo> = emptyList(),
    val initialPhotoId: Long = -1L,
    val coverPhotoId: Long? = null,
)
