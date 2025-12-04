package com.example.ecomarket.domain.repository

import com.example.ecomarket.R
import com.example.ecomarket.data.models.Product

class ProductRepository {

    fun getProducts(): List<Product> {
        return listOf(
            Product(1, "Aceite de Oliva Extra Virgen", 2.990, R.drawable.aceite),
            Product(2, "Café Tostado y Molido Orgánico", 7.500, R.drawable.cafe),
            Product(3, "Granola Artesanal con Frutos Secos", 4.250, R.drawable.granola),
            Product(4, "Harina Integral Ecológica", 2.800, R.drawable.harina),
            Product(5, "Leche Entera de Campo (Litro)", 1.900, R.drawable.leche),
            Product(6, "Miel Multifloral Pura", 6.700, R.drawable.miel),
            Product(7, "Pan de Masa Madre (500g)", 3.500, R.drawable.pan),
            Product(8, "Té Verde en Hojas (Caja 20)", 4.100, R.drawable.te),
            Product(9, "Yogurt Natural Griego", 2.150, R.drawable.yogurt)
        )
    }
}