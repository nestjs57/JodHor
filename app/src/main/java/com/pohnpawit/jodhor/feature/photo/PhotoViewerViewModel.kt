package com.pohnpawit.jodhor.feature.photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pohnpawit.jodhor.core.navigation.Route
import com.pohnpawit.jodhor.data.repository.DormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DormRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.PhotoViewer>()
    private val dormId: Long = route.dormId
    private val initialPhotoId: Long = route.photoId

    val uiState: StateFlow<PhotoViewerUiState> = combine(
        repository.observePhotos(dormId),
        repository.observeDorm(dormId),
    ) { photos, dorm ->
        PhotoViewerUiState(
            isLoading = false,
            photos = photos,
            initialPhotoId = initialPhotoId,
            coverPhotoId = dorm?.coverPhotoId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PhotoViewerUiState(initialPhotoId = initialPhotoId),
    )

    fun deletePhoto(photoId: Long) {
        viewModelScope.launch { repository.deletePhoto(photoId) }
    }

    fun setCoverPhoto(photoId: Long) {
        viewModelScope.launch { repository.setCoverPhoto(dormId, photoId) }
    }
}
