package com.example.ecomarket.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.data.db.PurchaseDatabase

// Repositorios de Data (si los necesitas)
import com.example.ecomarket.data.repository.ShippingRepository as DataShippingRepository

// Repositorios de Domain
import com.example.ecomarket.domain.repository.ProductRepository
import com.example.ecomarket.domain.repository.PurchaseRepository

// Screens y ViewModels
import com.example.ecomarket.ui.screens.*
import com.example.ecomarket.ui.viewmodel.*

@Composable
fun EcoMarketApp(isLoggedIn: Boolean) {

    val navController = rememberNavController()
    val context = LocalContext.current

    // --- DEPENDENCIAS COMUNES ---
    val purchaseDao = PurchaseDatabase.getInstance(context).purchaseDao()
    val gson = Gson()
    val productRepository = ProductRepository()
    val purchaseRepository = PurchaseRepository(purchaseDao, gson)
    val userPrefsRepository = UserPreferencesRepository(context)
    // --- FIN DEPENDENCIAS COMUNES ---

    val startDestination =
        if (isLoggedIn) Screen.Main.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // 🔐 LOGIN
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(repository = userPrefsRepository)
            )

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
            val productsViewModel: ProductsViewModel = viewModel(
                factory = ProductsViewModelFactory(productRepository)
            )

            MainScreen(
                mainNavController = navController,
                productsViewModel = productsViewModel
            )
        }

        // 🛒 CARRITO
        composable(Screen.Cart.route) {
            val productsViewModel: ProductsViewModel = viewModel(
                factory = ProductsViewModelFactory(productRepository)
            )

            val checkoutViewModel: CheckoutViewModel = viewModel(
                factory = CheckoutViewModelFactory(
                    context,
                    productsViewModel,
                    userPrefsRepository,
                    purchaseRepository
                )
            )

            CartScreen(
                mainNavController = navController,
                viewModel = checkoutViewModel
            )
        }

        // 💳 CHECKOUT
        composable(Screen.Checkout.route) {
            val productsViewModel: ProductsViewModel = viewModel(
                factory = ProductsViewModelFactory(productRepository)
            )

            val checkoutViewModel: CheckoutViewModel = viewModel(
                factory = CheckoutViewModelFactory(
                    context,
                    productsViewModel,
                    userPrefsRepository,
                    purchaseRepository
                )
            )

            CheckoutScreen(
                mainNavController = navController,
                viewModel = checkoutViewModel
            )
        }

        // 🚚 SHIPPING
        composable(Screen.Shipping.route) {
            val shippingRepositoryImpl = DataShippingRepository(context)

            val shippingViewModel: ShippingViewModel = viewModel(
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
