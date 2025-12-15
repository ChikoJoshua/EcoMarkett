package com.example.ecomarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.data.models.CartItem
import com.example.ecomarket.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la UI para Checkout
data class CheckoutUiState(
    val selectedMethod: String? = null, // "Retiro en tienda" o "Despacho"
    val isRetiroSelected: Boolean = false,
    val isDespachoSelected: Boolean = false,
    val address: String = "",
    val name: String = "",
    val comuna: String = "",
    val number: String = "",
    val isProcessing: Boolean = false,
    val isPurchaseComplete: Boolean = false
)

class CheckoutViewModel(
    private val productsViewModel: ProductsViewModel,
    private val purchaseRepository: PurchaseRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    // Seleccionar método de envío
    fun setMethod(method: String) {
        val isRetiro = method == "Retiro en tienda"
        _uiState.update {
            it.copy(
                selectedMethod = method,
                isRetiroSelected = isRetiro,
                isDespachoSelected = !isRetiro,
                address = if (isRetiro) "Retiro en Sucursal" else "",
                name = if (isRetiro) "N/A" else ""
            )
        }
    }

    // Actualizar campos del formulario
    fun updateAddressField(newAddress: String) { _uiState.update { it.copy(address = newAddress) } }
    fun updateNameField(newName: String) { _uiState.update { it.copy(name = newName) } }
    fun updateComunaField(newComuna: String) { _uiState.update { it.copy(comuna = newComuna) } }
    fun updateNumberField(newNumber: String) { _uiState.update { it.copy(number = newNumber) } }

    // Validar formulario de despacho
    private fun isDespachoFormValid(): Boolean {
        return _uiState.value.address.isNotBlank() &&
                _uiState.value.name.isNotBlank() &&
                _uiState.value.comuna.isNotBlank() &&
                _uiState.value.number.isNotBlank()
    }

    // Finalizar compra
    fun finalizePurchase() {
        // Si despacho seleccionado pero formulario inválido, no continuar
        if (_uiState.value.isDespachoSelected && !isDespachoFormValid()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }

            val cartItems = productsViewModel.uiState.value.cartItems
            val total = productsViewModel.getCartTotal()
            val userEmail = userPrefsRepository.userEmail.first() ?: "unknown"
            val method = _uiState.value.selectedMethod ?: "N/A"

            val canContinue = _uiState.value.isRetiroSelected || (_uiState.value.isDespachoSelected && isDespachoFormValid())
            val needsShippingData = _uiState.value.isDespachoSelected && !isDespachoFormValid()

            val finalAddress = if (_uiState.value.isDespachoSelected) {
                "Dirección: ${_uiState.value.address}, Comuna: ${_uiState.value.comuna}, Nombre: ${_uiState.value.name}, Número: ${_uiState.value.number}"
            } else {
                "Retiro en Sucursal Central"
            }

            purchaseRepository.registerPurchase(
                userEmail = userEmail,
                cartItems = cartItems,
                total = total,
                method = method,
                address = finalAddress
            )

            productsViewModel.clearCart()

            _uiState.update { it.copy(isProcessing = false, isPurchaseComplete = true) }
        }
    }
}
