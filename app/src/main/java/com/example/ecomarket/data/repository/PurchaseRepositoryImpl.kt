package com.example.ecomarket.data.repository

import com.example.ecomarket.data.models.CartItem

class PurchaseRepositoryImpl {

    fun registerPurchase(
        userEmail: String,
        cartItems: List<CartItem>,
        total: Double,
        method: String,
        address: String
    ) {

        println("Compra registrada para $userEmail, total: $total, método: $method, dirección: $address")
    }
}
