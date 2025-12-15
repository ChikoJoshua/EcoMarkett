package com.example.ecomarket.data.repository

import com.example.ecomarket.data.models.CartItem

class PurchaseRepository {

    fun registerPurchase(
        userEmail: String,
        cartItems: List<CartItem>,
        total: Double,
        method: String,
        address: String
    ) {
        // Aquí normalmente guardas en DB o SharedPreferences
        println("Compra registrada para $userEmail, total: $total, método: $method, dirección: $address")
    }
}
