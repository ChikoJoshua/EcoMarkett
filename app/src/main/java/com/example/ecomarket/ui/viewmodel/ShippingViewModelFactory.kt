package com.example.ecomarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecomarket.data.repository.ShippingRepository

class ShippingViewModelFactory(
    private val shippingRepository: ShippingRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShippingViewModel::class.java)) {
            return ShippingViewModel(shippingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
