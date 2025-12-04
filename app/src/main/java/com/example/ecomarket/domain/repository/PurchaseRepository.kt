package com.example.ecomarket.domain.repository

import com.example.ecomarket.data.db.PurchaseDao
import com.example.ecomarket.data.db.PurchaseEntity
import com.example.ecomarket.data.models.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import java.util.Date

class PurchaseRepository(
    private val purchaseDao: PurchaseDao,
    private val gson: Gson
) {

    suspend fun registerPurchase(
        userEmail: String,
        cartItems: List<CartItem>,
        total: Double,
        method: String,
        address: String?
    ) {

        val productsJson = gson.toJson(cartItems)

        val purchase = PurchaseEntity(
            userEmail = userEmail,
            productsDetailJson = productsJson,
            total = total,
            method = method,
            date = Date(),
            address = address
        )
        purchaseDao.insertPurchase(purchase)
    }


    fun getPurchaseHistory(): Flow<List<PurchaseEntity>> {
        return purchaseDao.getAllPurchases()
    }


    suspend fun getPurchaseCount(email: String): Int {
        return purchaseDao.countPurchasesByEmail(email)
    }


    fun getCartItemsFromEntity(entity: PurchaseEntity): List<CartItem> {
        val listType = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(entity.productsDetailJson, listType)
    }
}