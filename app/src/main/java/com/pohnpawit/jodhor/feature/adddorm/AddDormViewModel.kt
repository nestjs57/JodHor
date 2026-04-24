package com.pohnpawit.jodhor.feature.adddorm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pohnpawit.jodhor.core.navigation.Route
import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.repository.DormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DormRepository,
) : ViewModel() {

    private val dormId: Long? = savedStateHandle.toRoute<Route.AddDorm>().dormId

    private val _uiState = MutableStateFlow(AddDormUiState(isNew = dormId == null))
    val uiState: StateFlow<AddDormUiState> = _uiState.asStateFlow()

    init {
        if (dormId != null) load(dormId)
    }

    private fun load(id: Long) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val dorm = repository.getDorm(id) ?: run {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isNew = false,
                    name = dorm.name,
                    address = dorm.address,
                    priceMonthly = dorm.priceMonthly?.toString().orEmpty(),
                    contactPhone = dorm.contactPhone,
                    mapUrl = dorm.mapUrl,
                    notes = dorm.notes,
                )
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onAddressChange(value: String) = _uiState.update { it.copy(address = value) }
    fun onPriceChange(value: String) =
        _uiState.update { it.copy(priceMonthly = value.filter(Char::isDigit)) }
    fun onPhoneChange(value: String) = _uiState.update { it.copy(contactPhone = value) }
    fun onMapUrlChange(value: String) = _uiState.update { it.copy(mapUrl = value) }
    fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }

    fun save(onDone: () -> Unit) {
        val s = _uiState.value
        if (!s.canSave) return
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            repository.upsertDorm(
                Dorm(
                    id = dormId ?: 0L,
                    name = s.name.trim(),
                    address = s.address.trim(),
                    priceMonthly = s.priceMonthly.toIntOrNull(),
                    contactPhone = s.contactPhone.trim(),
                    mapUrl = s.mapUrl.trim(),
                    notes = s.notes.trim(),
                )
            )
            onDone()
        }
    }
}
