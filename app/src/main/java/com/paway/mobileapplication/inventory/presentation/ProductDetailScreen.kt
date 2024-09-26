package com.paway.mobileapplication.inventory.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.inventory.domain.Product

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val product = viewModel.product.value
    val stockDesired = viewModel.stockDesired.value

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffd9d9d9)),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = product.name,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                EditableCard(
                    title = "Stock Actual:",
                    value = product.stock.toString(),
                    onValueChanged = viewModel::onStockChanged
                )

                Spacer(modifier = Modifier.height(8.dp))


                EditableCard(
                    title = "Proveedor:",
                    value = "Desconocido",
                    onValueChanged = {}
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                EditableCard(
                    title = "Descripción:",
                    value = "Sin descripción", // Esto también puede ser dinámico
                    onValueChanged = {}
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Stock Deseable
                EditableCard(
                    title = "Stock Deseable:",
                    value = stockDesired,
                    onValueChanged = viewModel::onStockDesiredChanged
                )

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { viewModel.saveChanges(onSave) }) {
                        Text("Guardar")
                    }
                    Button(onClick = { viewModel.cancelChanges(onCancel) }) {
                        Text("Cancelar")
                    }
                }
            }
        }
    )
}

@Composable
fun EditableCard(
    title: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xff3f3f3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f)) // Color del texto más claro
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChanged,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
