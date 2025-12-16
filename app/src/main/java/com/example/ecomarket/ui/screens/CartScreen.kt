package com.example.ecomarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecomarket.data.models.CartItem
import com.example.ecomarket.ui.Screen
import com.example.ecomarket.ui.viewmodel.CheckoutViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    mainNavController: NavHostController,
    viewModel: CheckoutViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartItems = uiState.cartItems

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito de Compras") },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearCart() }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Vaciar Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = {
            CartBottomBar(
                total = viewModel.getCartTotal(),
                onContinueClick = { mainNavController.navigate(Screen.Checkout.route) },
                isEnabled = cartItems.isNotEmpty()
            )
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            EmptyCartMessage(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onUpdateQuantity = { cartItem, quantity ->
                            viewModel.updateCartItemQuantity(cartItem, quantity)
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onUpdateQuantity: (CartItem, Int) -> Unit
) {
    val priceText = String.format(Locale.US, "$%.2f", item.product.price)
    val totalItemPrice = String.format(Locale.US, "$%.2f", item.product.price * item.quantity)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.product.imageResId),
            contentDescription = item.product.name,
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.product.name, style = MaterialTheme.typography.titleMedium)
            Text("Precio unitario: $priceText")
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                totalItemPrice,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (item.quantity > 1) onUpdateQuantity(item, item.quantity - 1) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Quitar")
                }

                Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))

                IconButton(onClick = { onUpdateQuantity(item, item.quantity + 1) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Sumar")
                }
            }
        }
    }
}

@Composable
fun CartBottomBar(
    total: Double,
    onContinueClick: () -> Unit,
    isEnabled: Boolean
) {
    val totalText = String.format(Locale.US, "$%.2f", total)

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total de la compra:")
            Text(
                totalText,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.tertiary
                )
            )
        }

        Button(
            onClick = onContinueClick,
            enabled = isEnabled,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Continuar con la compra")
        }
    }
}

@Composable
fun EmptyCartMessage(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito Vacío", modifier = Modifier.size(80.dp))

        Text(
            "Tu carrito está vacío.",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text("¡Agrega algunos productos para continuar!")
    }
}
