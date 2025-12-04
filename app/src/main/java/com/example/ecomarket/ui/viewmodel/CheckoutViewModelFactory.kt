package com.example.ecomarket.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.data.db.AppDatabase
import com.example.ecomarket.domain.repository.PurchaseRepository
import com.google.gson.Gson

class CheckoutViewModelFactory(
    private val context: Context,
    private val productsViewModel: ProductsViewModel
) : ViewModelProvider.Factory {


    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "ecomarket-db"
        ).build()
    }

    private val purchaseDao by lazy { db.purchaseDao() }

    private val purchaseRepository by lazy {
        PurchaseRepository(purchaseDao, Gson())
    }

    private val userPrefsRepository by lazy {
        UserPreferencesRepository(context.applicationContext)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(
                productsViewModel = productsViewModel,
                purchaseRepository = purchaseRepository,
                userPrefsRepository = userPrefsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
