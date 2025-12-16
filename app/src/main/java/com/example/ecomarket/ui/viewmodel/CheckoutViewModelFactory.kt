package com.example.ecomarket.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.domain.repository.PurchaseRepository

class CheckoutViewModelFactory(
    private val context: Context,
    private val productsViewModel: ProductsViewModel,
    private val userPrefsRepository: UserPreferencesRepository,
    private val purchaseRepository: PurchaseRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            return CheckoutViewModel(
                context,
                productsViewModel,
                purchaseRepository,
                userPrefsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
