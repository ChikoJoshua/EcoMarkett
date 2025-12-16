package com.example.ecomarket.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.data.models.CartItem
import com.example.ecomarket.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val context: Context,
    private val productsViewModel: ProductsViewModel,
    private val purchaseRepository: PurchaseRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    // Delegamos el estado del carrito
    val uiState: StateFlow<ProductsUiState> = productsViewModel.uiState

    // Métodos para manejar carrito
    fun getCartTotal(): Double = productsViewModel.getCartTotal()

    fun updateCartItemQuantity(item: CartItem, quantity: Int) {
        productsViewModel.updateCartItemQuantity(item, quantity)
    }

    fun clearCart() {
        productsViewModel.clearCart()
    }

    // Variables necesarias para CheckoutScreen
    var isRetiroSelected by mutableStateOf(true)
    var isDespachoSelected by mutableStateOf(false)
    var address by mutableStateOf("")
    var name by mutableStateOf("")
    var comuna by mutableStateOf("")
    var number by mutableStateOf("")

    var isProcessing by mutableStateOf(false)
    var isPurchaseComplete by mutableStateOf(false)

    // Cambiar método de envío
    fun setMethod(retiro: Boolean) {
        isRetiroSelected = retiro
        isDespachoSelected = !retiro
    }

    // Finalizar compra
    fun finalizePurchase() {
        viewModelScope.launch {
            isProcessing = true
            try {
                val method = if (isRetiroSelected) "Retiro" else "Despacho"
                val fullAddress = if (isDespachoSelected) "$address, $comuna, $number" else null
                val userEmail = userPrefsRepository.getUserEmail()
                purchaseRepository.registerPurchase(
                    userEmail = userEmail,
                    cartItems = uiState.value.cartItems,
                    total = getCartTotal(),
                    method = method,
                    address = fullAddress
                )
                isPurchaseComplete = true
                clearCart()
            } finally {
                isProcessing = false
            }
        }
    }
}
