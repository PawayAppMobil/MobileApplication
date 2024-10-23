package com.paway.mobileapplication.inventory.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.inventory.domain.Invoice

@Composable
fun InvoiceListScreen(viewModel: InvoiceListViewModel, modifier: Modifier = Modifier) {
    val statePagar by viewModel.statePagar.collectAsState()
    val statePagado by viewModel.statePagado.collectAsState()
    val totalQuantityPagar by viewModel.totalQuantityPagar.collectAsState()
    val totalAmountPagar by viewModel.totalAmountPagar.collectAsState()
    val totalQuantityPagado by viewModel.totalQuantityPagado.collectAsState()
    val totalAmountPagado by viewModel.totalAmountPagado.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadInvoices()
    }

    Scaffold(modifier = modifier) { paddingValues: PaddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Sección de saldo total
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Saldo Total",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "S/ 10,000.00",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )

            // Sección de artículos críticos
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Artículos Críticos",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "3",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.error
            )

            // Sección de facturas por cobrar
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Facturas por Cobrar",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            if (statePagar.isLoading) {
                CircularProgressIndicator()
            } else if (statePagar.error.isNotEmpty()) {
                Text(statePagar.error)
            } else {
                statePagar.data?.let { invoices: List<Invoice> ->
                    LazyColumn {
                        /*
                        items(invoices) { invoice ->
                            Text("Invoice ID: ${invoice.id}")
                            Text("Amount: ${invoice.amount}")
                            // Mostrar otros detalles de la factura aquí
                        }

                         */
                    }
                    // Mostrar la suma de las cantidades y el monto total
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Total Quantity for pagar: $totalQuantityPagar",
                        fontSize = 20.sp
                    )
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Total Amount for pagar: $totalAmountPagar",
                        fontSize = 20.sp
                    )
                }
            }

            // Franja divisoria
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Sección de facturas por pagar
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Facturas por Pagar",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            if (statePagado.isLoading) {
                CircularProgressIndicator()
            } else if (statePagado.error.isNotEmpty()) {
                Text(statePagado.error)
            } else {
                statePagado.data?.let { invoices: List<Invoice> ->
                    LazyColumn {
                        items(invoices) { invoice ->
                            Text("Invoice ID: ${invoice.id}")
                            Text("Amount: ${invoice.amount}")
                            // Mostrar otros detalles de la factura aquí
                        }
                    }
                    // Mostrar la suma de las cantidades y el monto total
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Total Quantity for pagado: $totalQuantityPagado",
                        fontSize = 20.sp
                    )
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Total Amount for pagado: $totalAmountPagado",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}