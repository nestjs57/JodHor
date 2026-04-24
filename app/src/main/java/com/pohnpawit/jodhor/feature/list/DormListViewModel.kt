package com.pohnpawit.jodhor.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.repository.DormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DormListViewModel @Inject constructor(
    private val repository: DormRepository,
) : ViewModel() {

    private val filter = MutableStateFlow(DormListUiState.Filter.ALL)

    val uiState: StateFlow<DormListUiState> = combine(
        repository.observeDormPreviews(),
        filter,
    ) { previews, current ->
        val filtered = when (current) {
            DormListUiState.Filter.ALL -> previews
            DormListUiState.Filter.PLANNED -> previews.filter { it.dorm.status == DormStatus.PLANNED }
            DormListUiState.Filter.VIEWED -> previews.filter { it.dorm.status == DormStatus.VIEWED }
            DormListUiState.Filter.FAVORITES -> previews.filter { it.dorm.isFavorite }
        }
        DormListUiState(isLoading = false, dorms = filtered, filter = current)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DormListUiState(),
    )

    fun setFilter(value: DormListUiState.Filter) {
        filter.value = value
    }

    fun toggleFavorite(id: Long, isFavorite: Boolean) {
        viewModelScope.launch { repository.setFavorite(id, isFavorite) }
    }

    fun toggleStatus(id: Long, currentStatus: DormStatus) {
        viewModelScope.launch {
            val next = if (currentStatus == DormStatus.VIEWED) DormStatus.PLANNED else DormStatus.VIEWED
            repository.setStatus(id, next)
        }
    }
}
