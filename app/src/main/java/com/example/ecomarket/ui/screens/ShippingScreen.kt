package com.example.ecomarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecomarket.ui.viewmodel.ShippingViewModel

@Composable
fun ShippingScreen(
    viewModel: ShippingViewModel,
    onConfirm: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Datos de Envío") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = viewModel::updateFullName,
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.address,
                onValueChange = viewModel::updateAddress,
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.city,
                onValueChange = viewModel::updateCity,
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.phone,
                onValueChange = viewModel::updatePhone,
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.saveData,
                    onCheckedChange = viewModel::toggleSaveData
                )
                Text("Guardar datos para futuras compras")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.confirmShipping(onConfirm) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Envío")
            }
        }
    }
}
