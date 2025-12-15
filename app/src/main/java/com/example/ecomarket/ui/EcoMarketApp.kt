package com.example.ecomarket.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import com.google.gson.Gson
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecomarket.data.db.PurchaseDatabase
import com.example.ecomarket.data.datastore.UserPreferencesRepository

// IMPORTS CLAVE: Usamos alias para la capa DATA para evitar conflictos con DOMAIN
import com.example.ecomarket.data.repository.PurchaseRepository as DataPurchaseRepository // CLASE CONCRETA (data)
import com.example.ecomarket.data.repository.ShippingRepository as DataShippingRepository // CLASE CONCRETA (data)

// Importaciones de Interfaces de Repositorio (Dominio)
import com.example.ecomarket.domain.repository.PurchaseRepository // INTERFAZ (domain)
import com.example.ecomarket.domain.repository.ShippingRepository // INTERFAZ (domain)

// Importaciones de Vistas y ViewModels
import com.example.ecomarket.ui.screens.*
import com.example.ecomarket.ui.viewmodel.*

@Composable
fun EcoMarketApp(isLoggedIn: Boolean) {

    val navController = rememberNavController()
    val context = LocalContext.current

    // --- DEPENDENCIAS COMUNES ---
    val purchaseDao = PurchaseDatabase.getInstance(context).purchaseDao()
    val gson = Gson()
    // --- FIN DEPENDENCIAS COMUNES ---


    val startDestination =
        if (isLoggedIn) Screen.Main.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // 🔐 LOGIN
        composable(Screen.Login.route) {
            val repository = UserPreferencesRepository(context)

            val loginViewModel: LoginViewModel =
                viewModel(factory = LoginViewModelFactory(repository = repository))

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 🏠 MAIN
        composable(Screen.Main.route) {
            val productsViewModel: ProductsViewModel =
                viewModel(factory = ProductsViewModelFactory())

            MainScreen(
                mainNavController = navController,
                productsViewModel = productsViewModel
            )
        }

        // 🛒 CARRITO
        composable(Screen.Cart.route) {
            val productsViewModel: ProductsViewModel =
                viewModel(factory = ProductsViewModelFactory())
            val userPrefsRepository = UserPreferencesRepository(context)

            // DI: Construir la CLASE CONCRETA y ASIGNARLA a la INTERFAZ (PurchaseRepository)
            val purchaseRepository: PurchaseRepository = DataPurchaseRepository(
                purchaseDao,
                gson
            )

            val checkoutViewModel: CheckoutViewModel = viewModel(
                factory = CheckoutViewModelFactory(
                    context,
                    productsViewModel,
                    userPrefsRepository = userPrefsRepository,
                    purchaseRepository = purchaseRepository
                )
            )

            CartScreen(
                mainNavController = navController,
                viewModel = checkoutViewModel // <-- Nombre de parámetro corregido
            )
        }

        // 💳 CHECKOUT
        composable(Screen.Checkout.route) {
            val productsViewModel: ProductsViewModel =
                viewModel(factory = ProductsViewModelFactory())
            val userPrefsRepository = UserPreferencesRepository(context)

            // DI: Construir la CLASE CONCRETA y ASIGNARLA a la INTERFAZ (PurchaseRepository)
            val purchaseRepository: PurchaseRepository = DataPurchaseRepository(
                purchaseDao,
                gson
            )

            val checkoutViewModel: CheckoutViewModel = viewModel(
                factory = CheckoutViewModelFactory(
                    context,
                    productsViewModel,
                    userPrefsRepository = userPrefsRepository,
                    purchaseRepository = purchaseRepository
                )
            )

            CheckoutScreen(
                mainNavController = navController,
                viewModel = checkoutViewModel // <-- Nombre de parámetro corregido
            )
        }

        // 🚚 SHIPPING
        composable(Screen.Shipping.route) {
            // Instanciamos la implementación concreta
            val shippingRepositoryImpl = DataShippingRepository(context)

            val shippingViewModel: ShippingViewModel = viewModel(
                // Pasamos la implementación concreta.
                factory = ShippingViewModelFactory(shippingRepositoryImpl)
            )

            ShippingScreen(
                mainNavController = navController
            )
        }

        // 📜 HISTORY
        composable(Screen.History.route) {
            HistoryScreen()
        }
    }
}