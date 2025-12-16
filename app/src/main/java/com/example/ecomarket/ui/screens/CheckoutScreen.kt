package com.example.ecomarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecomarket.ui.viewmodel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    mainNavController: NavHostController,
    viewModel: CheckoutViewModel
) {
    val isProcessing = viewModel.isProcessing
    val isPurchaseComplete = viewModel.isPurchaseComplete

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Finalizar Compra", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Método de envío
        Row {
            RadioButton(
                selected = viewModel.isRetiroSelected,
                onClick = { viewModel.setMethod(true) }
            )
            Text("Retiro en tienda", modifier = Modifier.padding(start = 8.dp))

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = viewModel.isDespachoSelected,
                onClick = { viewModel.setMethod(false) }
            )
            Text("Despacho a domicilio", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isDespachoSelected) {
            OutlinedTextField(
                value = viewModel.address,
                onValueChange = { viewModel.address = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.comuna,
                onValueChange = { viewModel.comuna = it },
                label = { Text("Comuna") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.number,
                onValueChange = { viewModel.number = it },
                label = { Text("Número") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.finalizePurchase() },
            enabled = !isProcessing,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(if (isProcessing) "Procesando..." else "Finalizar Compra")
        }

        if (isPurchaseComplete) {
            Text("Compra realizada con éxito!", color = MaterialTheme.colorScheme.primary)
        }
    }
}
