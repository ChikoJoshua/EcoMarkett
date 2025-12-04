package com.example.ecomarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ecomarket.R
import com.example.ecomarket.domain.repository.ProductRepository
import com.example.ecomarket.ui.Screen
import com.example.ecomarket.ui.viewmodel.CheckoutViewModel
import com.example.ecomarket.ui.viewmodel.CheckoutViewModelFactory
import com.example.ecomarket.ui.viewmodel.ProductsViewModel
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import com.example.ecomarket.ui.viewmodel.ProductsViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(mainNavController: NavHostController) {

    val productsViewModel: ProductsViewModel = viewModel(factory = ProductsViewModelFactory(ProductRepository()))


    val context = LocalContext.current
    val checkoutViewModel: CheckoutViewModel = viewModel(
        factory = CheckoutViewModelFactory(context, productsViewModel)
    )
    val uiState by checkoutViewModel.uiState.collectAsState()

    if (uiState.isPurchaseComplete) {

        PurchaseCompleteScreen(mainNavController = mainNavController)
        return
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Finalizar Compra") }) },
        bottomBar = {
            CheckoutBottomBar(
                viewModel = checkoutViewModel,
                total = productsViewModel.getCartTotal()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("1. Elige Método de Entrega", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))


            DeliveryMethodSelector(viewModel = checkoutViewModel)

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isRetiroSelected) {
                // Opción Retiro: Mostrar sucursales para elegir (simulado)
                Text("2. Retiro en Tienda:", style = MaterialTheme.typography.titleLarge)
                Text("Sucursal Central: Calle Emaus 123", style = MaterialTheme.typography.bodyLarge)
                Text("Sucursal Norte: Avenida Siempre Viva 742", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Se seleccionará la Sucursal Central por defecto.", style = MaterialTheme.typography.bodyMedium)
            }

            if (uiState.isDespachoSelected) {

                DeliveryForm(viewModel = checkoutViewModel)
            }
        }
    }
}

@Composable
fun DeliveryMethodSelector(viewModel: CheckoutViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MethodButton(
            label = "Retiro en tienda 🏬",
            isSelected = viewModel.uiState.collectAsState().value.isRetiroSelected,
            onClick = { viewModel.setMethod("Retiro en tienda") }
        )
        MethodButton(
            label = "Despacho a domicilio 🚚",
            isSelected = viewModel.uiState.collectAsState().value.isDespachoSelected,
            onClick = { viewModel.setMethod("Despacho a domicilio") }
        )
    }
}

@Composable
fun MethodButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface,

            contentColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline
        )
    ) {
        Text(label)
    }
}


@Composable
fun DeliveryForm(viewModel: CheckoutViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("2. Ingresa Datos de Despacho", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 8.dp))

        OutlinedTextField(
            value = uiState.name,
            onValueChange = viewModel::updateNameField,
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = uiState.address,
            onValueChange = viewModel::updateAddressField,
            label = { Text("Dirección (Calle, Número)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.comuna,
                onValueChange = viewModel::updateComunaField,
                label = { Text("Comuna") },
                modifier = Modifier.weight(1f).padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = uiState.number,
                onValueChange = viewModel::updateNumberField,
                label = { Text("Teléfono") },
                modifier = Modifier.weight(1f).padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun CheckoutBottomBar(viewModel: CheckoutViewModel, total: Double) {
    val uiState by viewModel.uiState.collectAsState()
    val totalText = String.format(java.util.Locale.US, "$%.2f", total)


    val isEnabled = uiState.selectedMethod != null &&
            (uiState.isRetiroSelected || (uiState.isDespachoSelected && uiState.address.isNotBlank() && uiState.name.isNotBlank()))

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total a Pagar:", style = MaterialTheme.typography.headlineSmall)
            Text(totalText, style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary))
        }

        Button(
            onClick = viewModel::finalizePurchase,
            enabled = isEnabled && !uiState.isProcessing,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (uiState.isProcessing) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
            } else {
                Text("Confirmar y Pagar")
            }
        }
    }
}


@Composable
fun PurchaseCompleteScreen(mainNavController: NavHostController) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de Eco-Market",
                modifier = Modifier.size(100.dp).padding(bottom = 24.dp)
            )


            Text("¡Gracias por su compra!", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
            Text("Su pedido ha sido registrado con éxito.", style = MaterialTheme.typography.titleMedium)


            Card(Modifier.fillMaxWidth(0.9f).padding(vertical = 24.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Boleta Electrónica", style = MaterialTheme.typography.titleSmall)
                    Divider(Modifier.padding(vertical = 4.dp))
                    Text("Detalles: Se enviaron a su correo electrónico.")
                }
            }

            Button(onClick = {

                mainNavController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Main.route) { inclusive = true }
                }
            }) {
                Text("Volver al Menú Principal")
            }
        }
    }
}