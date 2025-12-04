package com.example.ecomarket.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ecomarket.ui.screens.*
import com.example.ecomarket.ui.viewmodel.LoginViewModel

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {


        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }


        composable(Screen.Main.route) {
            MainScreen(navController)
        }


        composable(Screen.Cart.route) {
            CartScreen(navController)
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(navController)
        }
    }
}
