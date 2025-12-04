package com.example.ecomarket.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {

    // Requerimiento 5: Registrar compra
    @Insert
    suspend fun insertPurchase(purchase: PurchaseEntity)

    // Requerimiento 6: Mostrar todas las compras realizadas
    @Query("SELECT * FROM purchases ORDER BY date DESC")
    fun getAllPurchases(): Flow<List<PurchaseEntity>>

    // Requerimiento 7: Cantidad de compras realizadas
    @Query("SELECT COUNT(id) FROM purchases WHERE userEmail = :email")
    suspend fun countPurchasesByEmail(email: String): Int
}