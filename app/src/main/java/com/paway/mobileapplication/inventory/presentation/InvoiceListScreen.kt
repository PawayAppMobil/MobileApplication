package com.paway.mobileapplication.inventory.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.remote.InvoiceService
import com.paway.mobileapplication.inventory.data.repository.InvoiceRepository
import com.paway.mobileapplication.inventory.domain.Invoice
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            // Sección de facturas por pagar
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Facturas por Pagar",
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
                        items(invoices) { invoice ->
                            Card(modifier = Modifier.padding(4.dp)) {
                                Column {
                                    Text(
                                        modifier = Modifier.padding(4.dp),
                                        text = "Invoice ID: ${invoice.id}",
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        modifier = Modifier.padding(4.dp),
                                        text = "Amount: ${invoice.amount}"
                                    )
                                    Text(
                                        modifier = Modifier.padding(4.dp),
                                        text = "Status: ${invoice.status}"
                                    )
                                }
                            }
                        }
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

            // Sección de facturas pagadas
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Facturas Pagadas",
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
                            Card(modifier = Modifier.padding(4.dp)) {
                                Column {
                                    Text(
                                        modifier = Modifier.padding(4.dp),
                                        text = "Invoice ID: ${invoice.id}",
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        modifier = Modifier.padding(4.dp),
                                        text = "Amount: ${invoice.amount}"
                                    )
                                    Text(
                                        modifier = Modifier.padding(4.dp),
                                        text = "Status: ${invoice.status}"
                                    )
                                }
                            }
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

@Preview(showBackground = true)
@Composable
fun InvoiceListScreenPreview() {
    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(InvoiceService::class.java)
    val repository = InvoiceRepository(service)
    val viewModel = InvoiceListViewModel(repository)
    InvoiceListScreen(viewModel)
}
