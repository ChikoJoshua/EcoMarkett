

package com.example.ecomarket.data.repository

import android.content.Context
import com.example.ecomarket.domain.repository.ShippingRepository
import com.example.ecomarket.domain.model.ShippingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ShippingRepository(
    private val context: Context
) : ShippingRepository {


    override fun getShippingData(): Flow<ShippingData> {

        return flowOf(ShippingData())
    }

    override suspend fun saveShippingData(data: ShippingData) {

    }

}