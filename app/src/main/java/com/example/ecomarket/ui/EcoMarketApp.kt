package com.example.ecomarket.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import com.example.ecomarket.data.repository.PurchaseRepository
import com.example.ecomarket.data.repository.ShippingRepository
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.ui.screens.*
import com.example.ecomarket.ui.viewmodel.*

@Composable
fun EcoMarketApp(isLoggedIn: Boolean) {

    val navController = rememberNavController()
    val context = LocalContext.current

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
                viewModel(factory = LoginViewModelFactory(repository))

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
            val purchaseRepository = PurchaseRepository(context)
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
                checkoutViewModel = checkoutViewModel
            )
        }

        // 💳 CHECKOUT
        composable(Screen.Checkout.route) {
            val productsViewModel: ProductsViewModel =
                viewModel(factory = ProductsViewModelFactory())
            val userPrefsRepository = UserPreferencesRepository(context)
            val purchaseRepository = PurchaseRepository(context)
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
                checkoutViewModel = checkoutViewModel
            )
        }

        // 🚚 SHIPPING
        composable(Screen.Shipping.route) {
            val shippingRepository = ShippingRepository(context)
            val shippingViewModel: ShippingViewModel = viewModel(
                factory = ShippingViewModelFactory(shippingRepository)
            )

            ShippingScreen(
                viewModel = shippingViewModel,
                onConfirm = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        // 📜 HISTORY
        composable(Screen.History.route) {
            HistoryScreen() // Asumiendo que no requiere argumentos
        }
    }
}
