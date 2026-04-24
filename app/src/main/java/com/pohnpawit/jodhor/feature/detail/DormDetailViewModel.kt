package com.pohnpawit.jodhor.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pohnpawit.jodhor.core.navigation.Route
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.repository.DormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DormDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DormRepository,
) : ViewModel() {

    private val dormId: Long = savedStateHandle.toRoute<Route.DormDetail>().dormId

    val uiState: StateFlow<DormDetailUiState> = combine(
        repository.observeDorm(dormId),
        repository.observePhotos(dormId),
    ) { dorm, photos ->
        DormDetailUiState(isLoading = false, dorm = dorm, photos = photos)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DormDetailUiState(),
    )

    fun toggleFavorite() {
        val current = uiState.value.dorm ?: return
        viewModelScope.launch { repository.setFavorite(current.id, !current.isFavorite) }
    }

    fun toggleStatus() {
        val current = uiState.value.dorm ?: return
        val next = if (current.status == DormStatus.VIEWED) DormStatus.PLANNED else DormStatus.VIEWED
        viewModelScope.launch { repository.setStatus(current.id, next) }
    }

    fun addPhoto(filePath: String) {
        viewModelScope.launch { repository.addPhoto(dormId, filePath) }
    }

    fun deletePhoto(photoId: Long) {
        viewModelScope.launch { repository.deletePhoto(photoId) }
    }

    fun delete(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.deleteDorm(dormId)
            onDone()
        }
    }
}
