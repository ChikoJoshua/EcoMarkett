package com.example.ecomarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.ecomarket.ui.viewmodel.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(mainNavController: NavHostController) {

    val productsViewModel: ProductsViewModel =
        viewModel(factory = ProductsViewModelFactory(ProductRepository()))

    val context = LocalContext.current
    val checkoutViewModel: CheckoutViewModel = viewModel(
        factory = CheckoutViewModelFactory(context, productsViewModel)
    )

    val uiState by checkoutViewModel.uiState.collectAsState()

    // ✅ COMPRA FINALIZADA
    if (uiState.isPurchaseComplete) {
        PurchaseCompleteScreen(mainNavController)
        return
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Finalizar Compra") }) },
        bottomBar = {
            CheckoutBottomBar(
                total = productsViewModel.getCartTotal(),
                isEnabled = uiState.canContinue,
                isProcessing = uiState.isProcessing,
                onConfirm = {

                    // 🚚 DESPACHO Y FALTAN DATOS → SHIPPING
                    if (uiState.isDespachoSelected && uiState.needsShippingData) {
                        mainNavController.navigate(Screen.Shipping.route)
                    } else {
                        checkoutViewModel.finalizePurchase()
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "1. Método de Entrega",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(12.dp))

            DeliveryMethodSelector(viewModel = checkoutViewModel)

            Spacer(Modifier.height(24.dp))

            if (uiState.isRetiroSelected) {
                RetiroInfo()
            }

            if (uiState.isDespachoSelected) {
                Text(
                    "Los datos de despacho se solicitarán al continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/* ---------- COMPONENTES ---------- */

@Composable
fun DeliveryMethodSelector(viewModel: CheckoutViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MethodButton(
            label = "Retiro en tienda 🏬",
            isSelected = uiState.isRetiroSelected
        ) { viewModel.setMethod("Retiro") }

        MethodButton(
            label = "Despacho 🚚",
            isSelected = uiState.isDespachoSelected
        ) { viewModel.setMethod("Despacho") }
    }
}

@Composable
fun MethodButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = if (isSelected) 2.dp else 1.dp
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Text(label)
    }
}

@Composable
fun RetiroInfo() {
    Column {
        Text("2. Retiro en tienda", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Sucursal Central: Calle Emaus 123")
        Text("Sucursal Norte: Av. Siempre Viva 742")
        Text(
            "Se asignará la sucursal central",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/* ---------- BOTTOM BAR ---------- */

@Composable
fun CheckoutBottomBar(
    total: Double,
    isEnabled: Boolean,
    isProcessing: Boolean,
    onConfirm: () -> Unit
) {
    val totalText = String.format(Locale.US, "$%.2f", total)

    Column(Modifier.padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total:")
            Text(
                totalText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            enabled = isEnabled && !isProcessing,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Continuar")
            }
        }
    }
}

/* ---------- COMPRA FINALIZADA ---------- */

@Composable
fun PurchaseCompleteScreen(mainNavController: NavHostController) {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "¡Gracias por tu compra!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    mainNavController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            ) {
                Text("Volver al inicio")
            }
        }
    }
}
