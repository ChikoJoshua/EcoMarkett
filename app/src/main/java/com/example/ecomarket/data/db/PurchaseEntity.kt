package com.example.ecomarket.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// La tabla de historial de compras
@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Requerimiento 7 (para la cuenta)
    val userEmail: String,

    // Detalle de la compra (usamos String para simplificar la lista de productos)
    // En apps grandes, esto sería otra tabla (relación 1-N)
    val productsDetailJson: String,

    val total: Double,
    val method: String, // Retiro en tienda o Despacho a domicilio
    val date: Date,
    val address: String? // Nullable si es Retiro en tienda
)