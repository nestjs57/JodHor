package com.pohnpawit.jodhor.feature.list

import com.pohnpawit.jodhor.data.model.DormPreview

data class DormListUiState(
    val isLoading: Boolean = true,
    val dorms: List<DormPreview> = emptyList(),
    val filter: Filter = Filter.ALL,
) {
    enum class Filter { ALL, NOT_CONTACTED, CONTACTED, VIEWED, FAVORITES }
}
