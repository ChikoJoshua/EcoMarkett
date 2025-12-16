
package com.example.ecomarket.domain.repository

import kotlinx.coroutines.flow.Flow
import com.example.ecomarket.domain.model.ShippingData

interface ShippingRepository {

    fun getShippingData(): Flow<ShippingData>
    suspend fun saveShippingData(data: ShippingData)
    // ...
}