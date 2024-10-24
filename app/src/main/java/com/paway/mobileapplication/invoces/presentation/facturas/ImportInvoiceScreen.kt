package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
import java.util.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import com.google.gson.Gson
import com.google.gson.GsonBuilder

@Composable
fun ImportInvoiceScreen(viewModel: ImportInvoiceViewModel, userId: String?) {
    val state = viewModel.state.value
    val context = LocalContext.current

    var showJsonDialog by remember { mutableStateOf(false) }
    var jsonContent by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        userId?.let { viewModel.setUserId(it) }
    }

    val pickDocument = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val outputStream = ByteArrayOutputStream()
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            val byteArray = outputStream.toByteArray()
            viewModel.updateSelectedDocument(byteArray)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Create Invoice", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Invoice Status
        OutlinedTextField(
            value = state.invoice.status,
            onValueChange = { viewModel.updateInvoiceDetails(status = it) },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth()
        )

        // Due Date
        var dueDateText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = dueDateText,
            onValueChange = { 
                dueDateText = it
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = sdf.parse(it)
                    date?.let { parsedDate ->
                        viewModel.updateInvoiceDetails(dueDate = parsedDate)
                    }
                } catch (e: Exception) {
                    // Handle parsing error
                }
            },
            label = { Text("Due Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Document Upload
        Button(
            onClick = { pickDocument.launch("*/*") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (state.selectedDocument != null) "Document Selected" else "Upload Document")
        }

        // Invoice Items
        Text("Invoice Items", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(state.invoice.items) { item ->
                InvoiceItemRow(item, onRemove = { viewModel.removeInvoiceItem(item) })
            }
            item {
                AddInvoiceItemButton(onAdd = { viewModel.addInvoiceItem(it) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display total amount
        Text(
            "Total Amount: $${state.invoice.items.sumOf { it.quantity * it.unitPrice }}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.createInvoiceAndTransaction() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Create Invoice and Transaction")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    jsonContent = gson.toJson(state.invoice)
                    showJsonDialog = true
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Show JSON")
            }
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

        // Debug Info
        if (state.debugInfo.isNotEmpty()) {
            Text(
                "Debug Info:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                state.debugInfo,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    if (showJsonDialog) {
        AlertDialog(
            onDismissRequest = { showJsonDialog = false },
            title = { Text("JSON Preview") },
            text = { Text(jsonContent) },
            confirmButton = {
                Button(onClick = { showJsonDialog = false }) {
                    Text("Close")
                }
            }
        )
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
