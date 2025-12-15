package com.example.ecomarket.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.ui.screens.*
import com.example.ecomarket.ui.viewmodel.LoginViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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

        // 🏠 MAIN (Bottom Navigation)
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }

        // 🛒 CARRITO
        composable(Screen.Cart.route) {
            CartScreen(mainNavController = navController)
        }

        // 💳 CHECKOUT
        composable(Screen.Checkout.route) {
            CheckoutScreen(mainNavController = navController)
        }

        // 🚚 SHIPPING (NUEVA PANTALLA)
        composable(Screen.Shipping.route) {
            ShippingScreen(mainNavController = navController)
        }
    }
}

/* ---------- LOGIN VIEWMODEL FACTORY ---------- */

class LoginViewModelFactory(
    private val repository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
