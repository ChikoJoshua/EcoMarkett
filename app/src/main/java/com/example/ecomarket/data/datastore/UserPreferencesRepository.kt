package com.example.ecomarket.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    val isLoggedIn = context.dataStore.data
        .map { it[PreferencesKeys.IS_LOGGED_IN] ?: false }

    val userEmail: kotlinx.coroutines.flow.Flow<String?> = context.dataStore.data
        .map { it[PreferencesKeys.USER_EMAIL] }

    suspend fun setLoggedIn(email: String) {
        context.dataStore.edit {
            it[PreferencesKeys.IS_LOGGED_IN] = true
            it[PreferencesKeys.USER_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    // ✅ Nuevo método para CheckoutViewModel
    suspend fun getUserEmail(): String {
        return context.dataStore.data.map { it[PreferencesKeys.USER_EMAIL] ?: "test@example.com" }.first()
    }
}
