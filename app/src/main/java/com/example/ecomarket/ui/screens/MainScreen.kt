package com.example.ecomarket.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ecomarket.R
import com.example.ecomarket.ui.Screen
import androidx.compose.material3.ExperimentalMaterial3Api


sealed class BottomScreen(val route: String, val label: String, val icon: ImageVector) {
    object Products : BottomScreen(Screen.Products.route, "Productos", Icons.Filled.ShoppingCart)
    object History : BottomScreen(Screen.History.route, "Historial", Icons.Filled.List)
    object Account : BottomScreen(Screen.Account.route, "Cuenta", Icons.Filled.AccountCircle)
}

val items = listOf(
    BottomScreen.Products,
    BottomScreen.History,
    BottomScreen.Account
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val bottomNavController = androidx.navigation.compose.rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Logo de la App
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Eco-Market")
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = isSelected,
                        onClick = {
                            bottomNavController.navigate(screen.route) {

                                popUpTo(bottomNavController.graph.startDestinationId) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        BottomNavGraph(
            navController = bottomNavController,
            modifier = Modifier.padding(paddingValues),
            mainNavController = navController
        )
    }
}


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainNavController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BottomScreen.Products.route,
        modifier = modifier
    ) {
        composable(BottomScreen.Products.route) {

            ProductsScreen(mainNavController = mainNavController)
        }
        composable(BottomScreen.History.route) {

            HistoryScreen()
        }
        composable(BottomScreen.Account.route) {

            AccountScreen(mainNavController = mainNavController)
        }
    }
}