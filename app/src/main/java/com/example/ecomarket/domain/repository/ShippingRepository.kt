// Archivo: com/example/ecomarket/domain/repository/ShippingRepository.kt

package com.example.ecomarket.domain.repository

import kotlinx.coroutines.flow.Flow
import com.example.ecomarket.domain.model.ShippingData

interface ShippingRepository {

    fun getShippingData(): Flow<ShippingData> // <-- Ahora se resuelve
    suspend fun saveShippingData(data: ShippingData) // <-- Ahora se resuelve
    // ...
}