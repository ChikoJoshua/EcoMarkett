package com.example.ecomarket.data.models

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageResId: Int // Referencia al drawable (R.drawable....)
)