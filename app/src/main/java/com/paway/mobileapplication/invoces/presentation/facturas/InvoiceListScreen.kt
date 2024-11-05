package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice

@Composable
fun InvoiceListScreen(viewModel: InvoiceListViewModel, userId: String? = null) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(userId) {
        if (userId.isNullOrEmpty()) {
            println("Warning: userId is null or empty")
        }
        userId?.let { viewModel.setUserId(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderA()
        Spacer(modifier = Modifier.height(16.dp))
        
        // Debug Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Debug Information",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("User ID: ${userId ?: "null"}")
                Text("Loading State: ${state.isLoading}")
                Text("Has Error: ${state.error != null}")
                Text("Invoices Count: ${state.invoices.size}")
            }
        }

        Text(
            "FACTURAS IMPORTADAS",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF006064)
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Cargando facturas para usuario: $userId")
                }
            }
            state.error != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar facturas",
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.error ?: "Error desconocido",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (state.error?.contains("Empty response body") == true) {
                            Text(
                                text = "El servidor respondió exitosamente pero sin datos",
                                color = Color(0xFF795548),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            state.invoices.isEmpty() -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No se encontraron facturas",
                            color = Color(0xFFE65100),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Usuario ID: $userId",
                            color = Color(0xFFE65100),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            else -> {
                Text(
                    "Facturas encontradas: ${state.invoices.size}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                InvoiceList(state.invoices)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        ActionButtons(viewModel)
    }
}

@Composable
fun InvoiceList(invoices: List<Invoice>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            items(invoices) { invoice ->
                InvoiceItem(invoice)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun InvoiceItem(invoice: Invoice) {
    Button(
        onClick = { /* Handle invoice selection */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Factura #${invoice.id}",
                color = Color.White
            )
            Text(
                text = "Monto: S/ ${invoice.amount}",
                color = Color.White
            )
            Text(
                text = "Estado: ${invoice.status}",
                color = Color.White
            )
            Text(
                text = "Fecha de vencimiento: ${invoice.dueDate}",
                color = Color.White
            )
        }
    }
}

@Composable
fun HeaderA() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "INVOICE LIST",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF006064)
        )
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color(0xFF006064)
        )
    }
}

@Composable
fun ActionButtons(viewModel: InvoiceListViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButton("Revisar Detalles") { viewModel.reviewDetails() }
        ActionButton("Confirmar") { viewModel.confirmInvoices() }
        ActionButton("Descartar") { viewModel.discardInvoices() }
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color.White)
    }
}
