package com.paway.mobileapplication.inventory.presentation


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp

import java.io.File

@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel,
    onBackClick: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var providerId by remember { mutableStateOf("") }
    var imageFile by remember { mutableStateOf<File?>(null) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    // Cargar el producto por ID
    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }
    val context = LocalContext.current
    // Obtener el estado del producto desde el ViewModel
    val productState by viewModel.state

    // Configurar lanzador para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val file = uriToFile(it, context)
            imageFile = file
        }
    }

    // Verificar el estado de la UI
    when {
        productState.isLoading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        productState.error.isNotEmpty() -> {
            errorMessage = productState.error
        }
        productState.data != null -> {
            val product = productState.data!!

            // Rellenar los campos con los valores actuales del producto
            // Estos valores son asignados a las variables, pero como son estados mutables,
            // el usuario puede modificarlos a través de los campos de entrada
            if (productName.isEmpty() && description.isEmpty() && price.isEmpty() && stock.isEmpty() && providerId.isEmpty()) {
                // Rellenar los campos con los valores actuales del producto solo si están vacíos
                description = product.description
                price = product.price.toString()
                productName = product.productName
                stock = product.stock.toString()
                providerId = product.providerId
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Campos de entrada para editar el producto
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },  // Permite modificar el valor
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },  // Permite modificar el valor
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },  // Permite modificar el valor
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },  // Permite modificar el valor
                    label = { Text("Stock") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = providerId,
                    onValueChange = { providerId = it },  // Permite modificar el valor
                    label = { Text("ProviderId") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Botón para seleccionar imagen
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Image")
                }

                imageFile?.let { file ->
                    Text("Selected file: ${file.name}", modifier = Modifier.padding(top = 8.dp))
                }

                // Mensajes de estado
                errorMessage.takeIf { it.isNotEmpty() }?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
                }
                successMessage.takeIf { it.isNotEmpty() }?.let {
                    Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(8.dp))
                }

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Botón de retroceso
                    Button(onClick = onBackClick) {
                        Text("Back")
                    }

                    // Botón para actualizar el producto
                    Button(
                        onClick = {
                            val priceDouble = price.toDoubleOrNull() ?: 0.0
                            val stockInt = stock.toIntOrNull() ?: 0

                            // Actualizar producto con los datos modificados
                            imageFile?.let {
                                loading = true
                                successMessage = ""
                                errorMessage = ""
                                viewModel.updateProduct(
                                    id = productId,
                                    description = description,
                                    price = priceDouble,
                                    productName = productName,
                                    stock = stockInt,
                                    providerId = providerId,
                                    imageFile = it,
                                    onSuccess = {
                                        successMessage = "Product updated successfully"
                                        loading = false
                                    },
                                    onError = {
                                        errorMessage = "Error updating product"
                                        loading = false
                                    }
                                )
                            }
                        },
                        enabled = description.isNotBlank() && productName.isNotBlank() && price.isNotBlank() && stock.isNotBlank() && imageFile != null
                    ) {
                        Text("Update Product")
                    }

                    // Botón Limpiar para borrar los campos de texto
                    Button(
                        onClick = {
                            // Resetear los campos a valores vacíos
                            description = ""
                            price = ""
                            productName = ""
                            stock = ""
                            providerId = ""
                            imageFile = null
                        }
                    ) {
                        Text("Clear")
                    }
                }
            }
        }
    }
}

