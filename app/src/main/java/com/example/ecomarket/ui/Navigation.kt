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

// CORRECCIÓN 1: AppNavigation debe recibir productsViewModel
@Composable
fun AppNavigation(
    navController: NavHostController,
    productsViewModel: ProductsViewModel // <-- Añadido el parámetro faltante
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

        // CORRECCIÓN 2: Pasar productsViewModel a MainScreen
        composable(Screen.Main.route) {
            // L33: Ahora pasamos el argumento que faltaba
            MainScreen(
                mainNavController = navController,
                productsViewModel = productsViewModel // <-- Añadido el argumento
            )
        }
    }
}