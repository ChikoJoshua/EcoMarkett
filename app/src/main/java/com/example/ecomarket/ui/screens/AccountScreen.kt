package com.example.ecomarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ecomarket.data.db.PurchaseEntity
import com.example.ecomarket.ui.Screen
import com.example.ecomarket.ui.viewmodel.AccountViewModel
import com.example.ecomarket.ui.viewmodel.AccountViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(mainNavController: NavHostController) {
    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(context))
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSessionCleared) {
        if (uiState.isSessionCleared) {
            mainNavController.navigate(Screen.Login.route) {
                popUpTo(Screen.Main.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Cuenta") }) }
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Usuario",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                InfoRow(
                    icon = Icons.Filled.Email,
                    label = "Correo",
                    value = uiState.userEmail ?: "No disponible"
                )

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                InfoRow(
                    icon = Icons.Filled.List,
                    label = "Compras realizadas",
                    value = uiState.purchaseCount.toString()
                )

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                HistorySummary(purchases = uiState.recentPurchases)

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = viewModel::logout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        Icons.Filled.ExitToApp,
                        contentDescription = "Cerrar Sesión",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun HistorySummary(purchases: List<PurchaseEntity>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Historial Reciente:",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (purchases.isEmpty()) {
            Text("No hay compras recientes.")
        } else {
            purchases.forEach { purchase ->
                RecentPurchaseItem(purchase)
            }
        }
    }
}

@Composable
fun RecentPurchaseItem(purchase: PurchaseEntity) {
    val dateFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val totalText = String.format(Locale.US, "$%.2f", purchase.total)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(dateFormatter.format(purchase.date))
                Text(purchase.method)
            }
            Text(
                totalText,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
