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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InvoiceListScreen(viewModel: InvoiceListViewModel, userId: String? = null) {
    val state by viewModel.state.collectAsState()
    var selectedInvoice by remember { mutableStateOf<Invoice?>(null) }

    LaunchedEffect(userId) {
        userId?.let { viewModel.setUserId(it) }
    }

    if (selectedInvoice != null) {
        InvoiceDetailDialog(
            invoice = selectedInvoice!!,
            onDismiss = { selectedInvoice = null },
            onConfirm = { viewModel.updateInvoice(it) },
            onDiscard = { viewModel.deleteInvoice(it.id) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderA()
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Debug Information",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
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

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    ErrorCard(state.error!!)
                }
                state.invoices.isEmpty() -> {
                    EmptyStateCard(userId)
                }
                else -> {
                    InvoiceList(
                        invoices = state.invoices,
                        onInvoiceClick = { selectedInvoice = it }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailDialog(
    invoice: Invoice,
    onDismiss: () -> Unit,
    onConfirm: (Invoice) -> Unit,
    onDiscard: (Invoice) -> Unit
) {
    var editedInvoice by remember { mutableStateOf(invoice) }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalle de Factura") },
        text = {
            Column {
                Text("ID: ${invoice.id}")
                Text("Usuario: ${invoice.userId}")
                Text("Monto: S/ ${invoice.amount}")
                Text("Estado: ${invoice.status}")
                Text("Fecha: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(invoice.date)}")
                Text("Fecha Vencimiento: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(invoice.dueDate)}")
                
                OutlinedTextField(
                    value = editedInvoice.status,
                    onValueChange = { editedInvoice = editedInvoice.copy(status = it) },
                    label = { Text("Estado") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cambiar Fecha Vencimiento")
                }

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            Button(onClick = {
                                showDatePicker = false
                            }) {
                                Text("OK")
                            }
                        }
                    ) {
                        DatePicker(
                            state = rememberDatePickerState(),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row {
                TextButton(onClick = { onConfirm(editedInvoice) }) {
                    Text("Confirmar")
                }
                TextButton(onClick = { onDiscard(invoice) }) {
                    Text("Descartar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun InvoiceList(
    invoices: List<Invoice>,
    onInvoiceClick: (Invoice) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .height(400.dp)
        ) {
            items(invoices) { invoice ->
                InvoiceItem(invoice, onInvoiceClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun InvoiceItem(
    invoice: Invoice,
    onInvoiceClick: (Invoice) -> Unit
) {
    Button(
        onClick = { onInvoiceClick(invoice) },
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
                text = "Fecha de vencimiento: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(invoice.dueDate)}",
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
fun ErrorCard(errorMessage: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB71C1C),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Error Icon",
                tint = Color.Red,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun EmptyStateCard(userId: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Empty State Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "No se encontraron facturas",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (userId != null) 
                    "No hay facturas registradas para el usuario: $userId" 
                else 
                    "Por favor, inicie sesión para ver sus facturas",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
