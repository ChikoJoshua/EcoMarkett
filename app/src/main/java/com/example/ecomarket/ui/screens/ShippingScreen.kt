package com.example.ecomarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.material.icons.filled.ArrowBack // <-- Import del icono
import com.example.ecomarket.domain.repository.ShippingRepository
import com.example.ecomarket.data.repository.ShippingRepository as DataShippingRepository // <-- Usamos el alias de data
import com.example.ecomarket.ui.Screen
import com.example.ecomarket.ui.viewmodel.ShippingViewModel
import com.example.ecomarket.ui.viewmodel.ShippingViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingScreen(
    mainNavController: NavHostController
) {
    val context = LocalContext.current

    // Inicializamos el ViewModel con su factory, usando la clase CONCRETA de Data
    val shippingRepositoryImpl = DataShippingRepository(context)

    val shippingViewModel: ShippingViewModel = viewModel(
        factory = ShippingViewModelFactory(shippingRepositoryImpl)
    )

    val uiState by shippingViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de Envío") },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.popBackStack() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        // ... (Resto del UI)
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {

            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = shippingViewModel::updateFullName,
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.address,
                onValueChange = shippingViewModel::updateAddress,
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.city,
                onValueChange = shippingViewModel::updateCity,
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.phone,
                onValueChange = shippingViewModel::updatePhone,
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.saveData,
                    onCheckedChange = shippingViewModel::toggleSaveData
                )
                Text("Guardar datos para futuras compras")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    shippingViewModel.confirmShipping {
                        mainNavController.navigate(Screen.Checkout.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Envío")
            }
        }
    }
}