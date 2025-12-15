package com.example.ecomarket.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ShippingData(
    val fullName: String = "",
    val address: String = "",
    val city: String = "",
    val phone: String = ""
)

class ShippingRepository {

    private val _shippingData = MutableStateFlow(ShippingData())
    val shippingData: StateFlow<ShippingData> = _shippingData

    fun saveShippingData(fullName: String, address: String, city: String, phone: String) {
        _shippingData.value = ShippingData(fullName, address, city, phone)
        println("Datos de envío guardados: $fullName, $address, $city, $phone")
    }
}
