package com.pohnpawit.jodhor.feature.list

import com.pohnpawit.jodhor.data.model.Dorm

data class DormListUiState(
    val isLoading: Boolean = true,
    val dorms: List<Dorm> = emptyList(),
    val filter: Filter = Filter.ALL,
) {
    enum class Filter { ALL, PLANNED, VIEWED, FAVORITES }
}
