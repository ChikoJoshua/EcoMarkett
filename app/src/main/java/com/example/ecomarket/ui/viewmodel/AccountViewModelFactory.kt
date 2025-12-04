package com.example.ecomarket.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.data.db.AppDatabase
import com.example.ecomarket.domain.repository.PurchaseRepository
import com.google.gson.Gson

class AccountViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "ecomarket-db"
    ).build()

    private val purchaseDao = db.purchaseDao()
    private val purchaseRepository = PurchaseRepository(purchaseDao, Gson())
    private val userPrefsRepository = UserPreferencesRepository(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(
                userPrefsRepository = userPrefsRepository,
                purchaseRepository = purchaseRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}