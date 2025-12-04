package com.example.ecomarket.ui

sealed class Screen(val route: String) {

    data object Login : Screen("login_screen")
    data object Main : Screen("main_screen")

    data object Products : Screen("products")
    data object History : Screen("history")
    data object Account : Screen("account")

    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
}
