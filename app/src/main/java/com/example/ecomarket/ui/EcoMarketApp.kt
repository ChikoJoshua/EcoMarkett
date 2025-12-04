package com.example.ecomarket.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.ui.screens.LoginScreen
import com.example.ecomarket.ui.viewmodel.LoginViewModel
import com.example.ecomarket.ui.screens.MainScreen

@Composable
fun EcoMarketApp(isLoggedIn: Boolean) {
    val navController = rememberNavController()


    val startDestination = if (isLoggedIn) Screen.Main.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {

            val context = LocalContext.current
            val repository = UserPreferencesRepository(context)
            val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository))

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

            MainScreen(navController = navController)
        }
    }
}


class LoginViewModelFactory(
    private val repository: UserPreferencesRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}