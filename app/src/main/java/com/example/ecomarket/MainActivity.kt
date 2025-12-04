package com.example.ecomarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.ui.EcoMarketApp
import com.example.ecomarket.ui.theme.EcoMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoMarketTheme {

                val context = LocalContext.current
                val userPrefsRepository = UserPreferencesRepository(context)


                val isLoggedIn by userPrefsRepository.isLoggedIn.collectAsState(initial = false)

                EcoMarketApp(isLoggedIn = isLoggedIn)
            }
        }
    }
}