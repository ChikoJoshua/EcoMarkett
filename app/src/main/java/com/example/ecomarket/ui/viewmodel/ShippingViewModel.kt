package com.example.ecomarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarket.data.repository.ShippingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShippingViewModel(
    private val repository: ShippingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShippingUiState())
    val uiState: StateFlow<ShippingUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.shippingData.collect { data ->
                _uiState.value = _uiState.value.copy(
                    fullName = data.fullName,
                    address = data.address,
                    city = data.city,
                    phone = data.phone
                )
            }
        }
    }

    fun updateFullName(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value)
    }

    fun updateAddress(value: String) {
        _uiState.value = _uiState.value.copy(address = value)
    }

    fun updateCity(value: String) {
        _uiState.value = _uiState.value.copy(city = value)
    }

    fun updatePhone(value: String) {
        _uiState.value = _uiState.value.copy(phone = value)
    }

    fun toggleSaveData(value: Boolean) {
        _uiState.value = _uiState.value.copy(saveData = value)
    }

    fun confirmShipping(onFinish: () -> Unit) {
        viewModelScope.launch {
            if (_uiState.value.saveData) {
                repository.saveShippingData(
                    _uiState.value.fullName,
                    _uiState.value.address,
                    _uiState.value.city,
                    _uiState.value.phone
                )
            }
            onFinish()
        }
    }
}
