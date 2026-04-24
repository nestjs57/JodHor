package com.pohnpawit.jodhor.feature.detail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pohnpawit.jodhor.core.navigation.Route
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.repository.DormRepository
import com.pohnpawit.jodhor.data.storage.PhotoFileStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DormDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DormRepository,
    private val photoFileStore: PhotoFileStore,
) : ViewModel() {

    private val dormId: Long = savedStateHandle.toRoute<Route.DormDetail>().dormId

    private var pendingCameraFile: File? = null
    private var pendingCoverFile: File? = null

    val uiState: StateFlow<DormDetailUiState> = combine(
        repository.observeDorm(dormId),
        repository.observePhotos(dormId),
        repository.observePhones(dormId),
        repository.observeCovers(dormId),
    ) { dorm, photos, phones, covers ->
        DormDetailUiState(
            isLoading = false,
            dorm = dorm,
            photos = photos,
            phones = phones,
            covers = covers,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DormDetailUiState(),
    )

    fun toggleFavorite() {
        val current = uiState.value.dorm ?: return
        viewModelScope.launch { repository.setFavorite(current.id, !current.isFavorite) }
    }

    fun toggleFull() {
        val current = uiState.value.dorm ?: return
        viewModelScope.launch { repository.setFull(current.id, !current.isFull) }
    }

    fun setStatus(status: DormStatus) {
        val current = uiState.value.dorm ?: return
        viewModelScope.launch { repository.setStatus(current.id, status) }
    }

    fun prepareCameraCapture(): Uri {
        val file = photoFileStore.createPhotoFile()
        pendingCameraFile = file
        return photoFileStore.uriFor(file)
    }

    fun onCameraResult(success: Boolean) {
        val file = pendingCameraFile ?: return
        pendingCameraFile = null
        if (success) {
            viewModelScope.launch { repository.addPhoto(dormId, file.absolutePath) }
        } else {
            viewModelScope.launch { photoFileStore.delete(file.absolutePath) }
        }
    }

    fun onPickedFromGallery(uris: List<Uri>) {
        if (uris.isEmpty()) return
        viewModelScope.launch {
            uris.forEach { uri ->
                val path = photoFileStore.copyFromUri(uri) ?: return@forEach
                repository.addPhoto(dormId, path)
            }
        }
    }

    fun deletePhoto(photoId: Long) {
        viewModelScope.launch { repository.deletePhoto(photoId) }
    }

    fun reorderPhotos(orderedIds: List<Long>) {
        viewModelScope.launch { repository.reorderPhotos(orderedIds) }
    }

    fun prepareCoverCapture(): Uri {
        val file = photoFileStore.createCoverFile()
        pendingCoverFile = file
        return photoFileStore.uriFor(file)
    }

    fun onCoverCameraResult(success: Boolean) {
        val file = pendingCoverFile ?: return
        pendingCoverFile = null
        if (success) {
            viewModelScope.launch { repository.addCover(dormId, file.absolutePath) }
        } else {
            viewModelScope.launch { photoFileStore.delete(file.absolutePath) }
        }
    }

    fun onCoverPickedFromGallery(uris: List<Uri>) {
        if (uris.isEmpty()) return
        viewModelScope.launch {
            uris.forEach { uri ->
                val path = photoFileStore.copyCoverFromUri(uri) ?: return@forEach
                repository.addCover(dormId, path)
            }
        }
    }

    fun deleteCover(coverId: Long) {
        viewModelScope.launch { repository.deleteCover(coverId) }
    }

    fun reorderCovers(orderedIds: List<Long>) {
        viewModelScope.launch { repository.reorderCovers(orderedIds) }
    }

    fun deleteDorm(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.deleteDorm(dormId)
            onDone()
        }
    }
}
