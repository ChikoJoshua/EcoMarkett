package com.example.ecomarket.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userEmail: String,

    val productsDetailJson: String,

    val total: Double,
    val method: String,
    val date: Date,
    val address: String?
)