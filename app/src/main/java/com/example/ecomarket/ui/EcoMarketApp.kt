package com.example.ecomarket.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.ui.screens.*
import com.example.ecomarket.ui.viewmodel.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
fun EcoMarketApp(isLoggedIn: Boolean) {

    val navController = rememberNavController()

    val startDestination =
        if (isLoggedIn) Screen.Main.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // 🔐 LOGIN
        composable(Screen.Login.route) {

            val context = LocalContext.current
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
            val productsViewModel: ProductsViewModel = viewModel(factory = ProductsViewModelFactory())
            MainScreen(
                viewModel = productsViewModel,
                navController = navController
            )
        }

        // 🛒 CARRITO
        composable(Screen.Cart.route) {
            val checkoutViewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory())
            CartScreen(
                viewModel = checkoutViewModel,
                mainNavController = navController
            )
        }

        // 💳 CHECKOUT
        composable(Screen.Checkout.route) {
            val checkoutViewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory())
            CheckoutScreen(
                viewModel = checkoutViewModel,
                mainNavController = navController
            )
        }

        // 🚚 SHIPPING
        composable(Screen.Shipping.route) {
            ShippingScreen(
                mainNavController = navController
                // Si ShippingScreen necesita un ViewModel, lo pasas aquí
            )
        }

        // 📜 HISTORY (si tienes pantalla de historial)
        composable(Screen.History.route) {
            val historyViewModel: HistoryViewModel = viewModel(factory = HistoryViewModelFactory())
            HistoryScreen(
                viewModel = historyViewModel,
                navController = navController
            )
        }
    }
}
