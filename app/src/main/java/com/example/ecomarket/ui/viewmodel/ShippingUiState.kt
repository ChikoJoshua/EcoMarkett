package com.example.ecomarket.ui.viewmodel

data class ShippingUiState(
    val fullName: String = "",
    val address: String = "",
    val city: String = "",
    val phone: String = "",
    val saveData: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)
