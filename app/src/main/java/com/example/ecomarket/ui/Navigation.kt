package com.example.ecomarket.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecomarket.ui.screens.*
import com.example.ecomarket.ui.viewmodel.LoginViewModel
import com.example.ecomarket.ui.viewmodel.ProductsViewModel // <-- NECESITAS EL IMPORT
import com.example.ecomarket.ui.viewmodel.ProductsViewModelFactory // <-- NECESITAS EL IMPORT

@Composable
fun AppNavigation(
    navController: NavHostController,
    productsViewModel: ProductsViewModel
) {

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
            MainScreen(
                mainNavController = navController,
                productsViewModel = productsViewModel
            )
        }
    }
}