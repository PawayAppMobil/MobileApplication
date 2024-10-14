package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.foundation.border
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
import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
import java.util.*

@Composable
fun ImportInvoiceScreen(viewModel: ImportInvoiceViewModel, userId: String?) {
    val state = viewModel.state.value

    LaunchedEffect(userId) {
        userId?.let { viewModel.setUserId(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Create Invoice", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Invoice Details
        OutlinedTextField(
            value = state.invoice.status,
            onValueChange = { viewModel.updateInvoiceDetails(status = it) },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth()
        )

        // Invoice Items
        LazyColumn {
            items(state.invoice.items) { item ->
                InvoiceItemRow(item, onRemove = { viewModel.removeInvoiceItem(item) })
            }
            item {
                AddInvoiceItemButton(onAdd = { viewModel.addInvoiceItem(it) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.createInvoiceAndTransaction() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Create Invoice and Transaction")
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }

        state.error?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        if (state.success) {
            Text("Invoice and Transaction created successfully!", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun InvoiceItemRow(item: InvoiceItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(item.description, modifier = Modifier.weight(1f))
        Text("${item.quantity} x ${item.unitPrice}")
        IconButton(onClick = onRemove) {
            Text("X")
        }
    }
}

@Composable
fun AddInvoiceItemButton(onAdd: (InvoiceItem) -> Unit) {
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = unitPrice,
                onValueChange = { unitPrice = it },
                label = { Text("Unit Price") },
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = {
                onAdd(
                    InvoiceItem(
                        id = UUID.randomUUID().toString(),
                        description = description,
                        quantity = quantity.toIntOrNull() ?: 0,
                        unitPrice = unitPrice.toDoubleOrNull() ?: 0.0,
                        productId = "" // You might want to handle this differently
                    )
                )
                description = ""
                quantity = ""
                unitPrice = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Item")
        }
    }
}
