package com.pohnpawit.jodhor.feature.adddorm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pohnpawit.jodhor.core.navigation.Route
import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.model.PhoneContact
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
        if (dormId != null) load(dormId) else _uiState.update { it.copy(phones = listOf(PhoneEntryUi())) }
    }

    private fun load(id: Long) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val dorm = repository.getDorm(id) ?: run {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }
            val phones = repository.getPhones(id)
                .map { PhoneEntryUi(number = it.number, note = it.note) }
                .ifEmpty { listOf(PhoneEntryUi()) }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isNew = false,
                    name = dorm.name,
                    address = dorm.address,
                    priceMonthly = dorm.priceMonthly?.toString().orEmpty(),
                    securityDeposit = dorm.securityDeposit?.toString().orEmpty(),
                    advancePayment = dorm.advancePayment?.toString().orEmpty(),
                    contractYears = dorm.contractYears?.toString().orEmpty(),
                    phones = phones,
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
    fun onDepositChange(value: String) =
        _uiState.update { it.copy(securityDeposit = value.filter(Char::isDigit)) }
    fun onAdvanceChange(value: String) =
        _uiState.update { it.copy(advancePayment = value.filter(Char::isDigit)) }
    fun onContractYearsChange(value: String) =
        _uiState.update { it.copy(contractYears = value.filter(Char::isDigit)) }
    fun onMapUrlChange(value: String) = _uiState.update { it.copy(mapUrl = value) }
    fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }

    fun onPhoneNumberChange(index: Int, value: String) = _uiState.update { current ->
        val digits = value.filter(Char::isDigit)
        current.copy(
            phones = current.phones.mapIndexed { i, entry ->
                if (i == index) entry.copy(number = digits) else entry
            },
        )
    }

    fun onPhoneNoteChange(index: Int, value: String) = _uiState.update { current ->
        current.copy(
            phones = current.phones.mapIndexed { i, entry ->
                if (i == index) entry.copy(note = value) else entry
            },
        )
    }

    fun addPhoneEntry() = _uiState.update { it.copy(phones = it.phones + PhoneEntryUi()) }

    fun removePhoneEntry(index: Int) = _uiState.update { current ->
        current.copy(
            phones = current.phones.toMutableList().apply { removeAt(index) }.ifEmpty {
                listOf(PhoneEntryUi())
            },
        )
    }

    fun save(onDone: () -> Unit) {
        val s = _uiState.value
        if (!s.canSave) return
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val id = repository.upsertDorm(
                Dorm(
                    id = dormId ?: 0L,
                    name = s.name.trim(),
                    address = s.address.trim(),
                    priceMonthly = s.priceMonthly.toIntOrNull(),
                    securityDeposit = s.securityDeposit.toIntOrNull(),
                    advancePayment = s.advancePayment.toIntOrNull(),
                    contractYears = s.contractYears.toIntOrNull(),
                    mapUrl = s.mapUrl.trim(),
                    notes = s.notes.trim(),
                )
            )
            val cleanPhones = s.phones
                .mapNotNull { entry ->
                    val number = entry.number.trim()
                    if (number.isBlank()) null
                    else PhoneContact(number = number, note = entry.note.trim())
                }
            repository.replaceDormPhones(id, cleanPhones)
            onDone()
        }
    }
}
