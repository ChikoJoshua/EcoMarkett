package com.example.ecomarket.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {

    @Insert
    suspend fun insertPurchase(purchase: PurchaseEntity)

    @Query("SELECT * FROM purchases ORDER BY date DESC")
    fun getAllPurchases(): Flow<List<PurchaseEntity>>

    @Query("SELECT COUNT(id) FROM purchases WHERE userEmail = :email")
    suspend fun countPurchasesByEmail(email: String): Int
}