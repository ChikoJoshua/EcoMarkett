package com.example.ecomarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarket.data.models.CartItem
import com.example.ecomarket.data.models.Product
import com.example.ecomarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val cartItemCount: Int = 0
)

class ProductsViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val products = productRepository.getProducts()
            _uiState.update { it.copy(products = products, isLoading = false) }
        }
    }

    fun addToCart(product: Product) {
        val currentCart = _uiState.value.cartItems.toMutableList()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            currentCart[currentCart.indexOf(existingItem)] = updatedItem
        } else {
            currentCart.add(CartItem(product, 1))
        }

        _uiState.update {
            it.copy(
                cartItems = currentCart,
                cartItemCount = currentCart.sumOf { item -> item.quantity }
            )
        }
    }

    fun getCartTotal(): Double {
        return _uiState.value.cartItems.sumOf { it.product.price * it.quantity }
    }

    fun updateCartItemQuantity(cartItem: CartItem, newQuantity: Int) {
        val currentCart = _uiState.value.cartItems.toMutableList()
        val index = currentCart.indexOfFirst { it.product.id == cartItem.product.id }

        if (index != -1) {
            if (newQuantity <= 0) {
                currentCart.removeAt(index)
            } else {
                currentCart[index] = cartItem.copy(quantity = newQuantity)
            }
        }

        _uiState.update {
            it.copy(
                cartItems = currentCart,
                cartItemCount = currentCart.sumOf { item -> item.quantity }
            )
        }
    }

    fun clearCart() {
        _uiState.update {
            it.copy(
                cartItems = emptyList(),
                cartItemCount = 0
            )
        }
    }
}
