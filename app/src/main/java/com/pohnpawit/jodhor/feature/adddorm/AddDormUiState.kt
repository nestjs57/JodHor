package com.pohnpawit.jodhor.feature.adddorm

data class AddDormUiState(
    val isLoading: Boolean = false,
    val isNew: Boolean = true,
    val name: String = "",
    val address: String = "",
    val priceMonthly: String = "",
    val securityDeposit: String = "",
    val advancePayment: String = "",
    val contractYears: String = "",
    val contactPhone: String = "",
    val mapUrl: String = "",
    val notes: String = "",
    val isSaving: Boolean = false,
) {
    val canSave: Boolean get() = name.isNotBlank() && !isSaving
}
