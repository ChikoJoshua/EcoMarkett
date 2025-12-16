package com.example.ecomarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.data.db.PurchaseEntity
import com.example.ecomarket.domain.repository.PurchaseRepository
import com.example.ecomarket.ui.viewmodel.HistoryViewModel
import com.example.ecomarket.ui.viewmodel.HistoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    purchaseRepository: PurchaseRepository,
    userPrefsRepository: UserPreferencesRepository
) {

    val context = LocalContext.current

    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(
            context,
            purchaseRepository,
            userPrefsRepository
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Historial de Compras") }) }
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

        } else if (uiState.purchases.isEmpty()) {
            EmptyHistoryMessage(Modifier.padding(paddingValues))

        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.purchases) { purchase ->
                    PurchaseCard(purchase = purchase)
                }
            }
        }
    }
}

@Composable
fun PurchaseCard(purchase: PurchaseEntity) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val totalText = String.format(Locale.US, "$%.2f", purchase.total)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                "Compra #${purchase.id}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "Fecha: ${dateFormatter.format(purchase.date)}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                "Método: ${purchase.method}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Pagado:", style = MaterialTheme.typography.titleMedium)
                Text(
                    totalText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
        }
    }
}

@Composable
fun EmptyHistoryMessage(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.List,
            contentDescription = "Historial Vacío",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            "Aún no tienes compras registradas.",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
