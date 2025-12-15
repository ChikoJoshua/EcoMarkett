package com.example.ecomarket.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("shipping_data")

class ShippingRepository(private val context: Context) {

    companion object {
        val FULL_NAME = stringPreferencesKey("full_name")
        val ADDRESS = stringPreferencesKey("address")
        val CITY = stringPreferencesKey("city")
        val PHONE = stringPreferencesKey("phone")
    }

    suspend fun saveShippingData(
        fullName: String,
        address: String,
        city: String,
        phone: String
    ) {
        context.dataStore.edit {
            it[FULL_NAME] = fullName
            it[ADDRESS] = address
            it[CITY] = city
            it[PHONE] = phone
        }
    }

    val shippingData = context.dataStore.data.map {
        ShippingData(
            fullName = it[FULL_NAME] ?: "",
            address = it[ADDRESS] ?: "",
            city = it[CITY] ?: "",
            phone = it[PHONE] ?: ""
        )
    }
}

data class ShippingData(
    val fullName: String,
    val address: String,
    val city: String,
    val phone: String
)
