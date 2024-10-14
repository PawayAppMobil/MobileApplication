package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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

@Composable
fun ImportInvoiceScreen(viewModel: ImportInvoiceViewModel) {
    var selectedFile by remember { mutableStateOf<String?>(null) }
    var invoiceType by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header()
        Spacer(modifier = Modifier.height(24.dp))
        FileUploadSection(
            selectedFile = selectedFile,
            onFileSelected = { selectedFile = it }
        )
        Spacer(modifier = Modifier.height(24.dp))
        InvoiceTypeSelection(
            selectedType = invoiceType,
            onTypeSelected = { invoiceType = it }
        )
        Spacer(modifier = Modifier.height(24.dp))
        ImportStatusSection()
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "IMPORT INVOICE",
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
fun FileUploadSection(
    selectedFile: String?,
    onFileSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("File Upload", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = selectedFile ?: "No file selected",
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Button(
                        onClick = { /* Implement file selection logic */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E))
                    ) {
                        Text("Browse")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Drag & Drop here\nOr\nBrowse", textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Implement upload logic */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                enabled = false
            ) {
                Text("Upload Now", color = Color.Gray)
            }
        }
    }
}

@Composable
fun InvoiceTypeSelection(
    selectedType: String?,
    onTypeSelected: (String) -> Unit
) {
    Column {
        Text("Invoice Type", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            RadioButton(
                selected = selectedType == "Purchase",
                onClick = { onTypeSelected("Purchase") }
            )
            Text("Purchase", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedType == "Sale",
                onClick = { onTypeSelected("Sale") }
            )
            Text("Sale", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun ImportStatusSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { /* Implement status check logic */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E))
        ) {
            Text("Estado de importación")
        }
        Button(
            onClick = { /* Implement success action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E))
        ) {
            Text("EXISTOSO")
        }
    }
}

// Placeholder for ViewModel
class ImportInvoiceViewModel {
    // Implement ViewModel logic here
    fun uploadInvoice(file: String, type: String) {
        // Implement invoice upload logic
    }

    fun createTransaction() {
        // Implement transaction creation logic
    }
}