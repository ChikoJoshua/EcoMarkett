// Archivo: com/example/ecomarket/ui/viewmodel/ShippingViewModel.kt

package com.example.ecomarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// IMPORTANTE: Cambiamos a la interfaz de DOMINIO
import com.example.ecomarket.domain.repository.ShippingRepository
// Importamos el modelo de datos que creamos
import com.example.ecomarket.domain.model.ShippingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update // Usamos update para manejar el StateFlow inmutable
import kotlinx.coroutines.launch

class ShippingViewModel(
    // 1. Usar la INTERFAZ de dominio
    private val repository: ShippingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShippingUiState())
    val uiState: StateFlow<ShippingUiState> = _uiState

    init {
        // 2. Corregir la llamada a Flow: El método es getShippingData()
        viewModelScope.launch {
            repository.getShippingData().collect { data ->
                _uiState.update { currentState ->
                    currentState.copy(
                        fullName = data.fullName,
                        address = data.address,
                        city = data.city,
                        phone = data.phone
                        // No inicializamos saveData desde el repo, ya que es un estado de UI/acción
                    )
                }
            }
        }
    }

    // Usamos .update para cambiar el StateFlow de manera segura
    fun updateFullName(value: String) {
        _uiState.update { it.copy(fullName = value) }
    }

    fun updateAddress(value: String) {
        _uiState.update { it.copy(address = value) }
    }

    fun updateCity(value: String) {
        _uiState.update { it.copy(city = value) }
    }

    fun updatePhone(value: String) {
        _uiState.update { it.copy(phone = value) }
    }

    fun toggleSaveData(value: Boolean) {
        _uiState.update { it.copy(saveData = value) }
    }

    fun confirmShipping(onFinish: () -> Unit) {
        viewModelScope.launch {
            if (_uiState.value.saveData) {
                // 3. Corregir la llamada al Repositorio para usar el modelo ShippingData
                val currentData = _uiState.value
                val dataToSave = ShippingData(
                    fullName = currentData.fullName,
                    address = currentData.address,
                    city = currentData.city,
                    phone = currentData.phone,
                    saveData = currentData.saveData
                )

                repository.saveShippingData(dataToSave)
            }
            onFinish()
        }
    }
}