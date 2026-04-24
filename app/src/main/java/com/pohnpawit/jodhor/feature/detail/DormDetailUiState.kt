package com.pohnpawit.jodhor.feature.detail

import com.pohnpawit.jodhor.data.model.CoverPhoto
import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.model.PhoneContact
import com.pohnpawit.jodhor.data.model.Photo

data class DormDetailUiState(
    val isLoading: Boolean = true,
    val dorm: Dorm? = null,
    val photos: List<Photo> = emptyList(),
    val covers: List<CoverPhoto> = emptyList(),
    val phones: List<PhoneContact> = emptyList(),
)
