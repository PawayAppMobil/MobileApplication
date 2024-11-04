package com.paway.mobileapplication.invoces.presentation.facturas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import com.paway.mobileapplication.inventory.domain.Product
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportInvoiceScreen(viewModel: ImportInvoiceViewModel, userId: String?) {
    val state = viewModel.state.value
    val context = LocalContext.current

    var showJsonDialog by remember { mutableStateOf(false) }
    var jsonContent by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        userId?.let { viewModel.setUserId(it) }
    }

    val pickDocument = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
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
        var dueDateText by remember { 
            mutableStateOf(
                state.invoice.dueDate?.let { 
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) 
                } ?: ""
            )
        }

        OutlinedTextField(
            value = dueDateText,
            onValueChange = { /* Readonly */ },
            label = { Text("Due Date") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            enabled = false,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            }
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Date(millis)
                            dueDateText = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(selectedDate)
                            viewModel.updateInvoiceDetails(dueDate = selectedDate)
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Document Upload
        Button(
            onClick = { pickDocument.launch("*/*") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (state.selectedDocument != null) "Document Selected" else "Upload Document")
        }

        // Products Selection
        Text(
            "Select Products",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(state.availableProducts) { product ->
                ProductSelectionItem(
                    product = product,
                    isSelected = product in state.selectedProducts,
                    onToggle = { viewModel.toggleProductSelection(product) }
                )
            }
        }

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
                    jsonContent = gson.toJson(state.invoice.toInvoiceDTO())
                    showJsonDialog = true
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Show JSON")
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        state.error?.let { error ->
            Text(
                error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (state.success) {
            Text(
                "Invoice and Transaction created successfully!",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

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
fun ProductSelectionItem(
    product: Product,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Stock: ${product.stock}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (product.isFavorite) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Favorite",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() }
        )
    }
}