package com.paway.mobileapplication.inventory.presentation

import android.os.Environment
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@Composable
fun ProductAddScreen(
    viewModel: ProductAddViewModel,

    onProductAdded: () -> Unit,
    onBackClick: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var providerId by remember { mutableStateOf("") }
    var imageFile by remember { mutableStateOf<File?>(null) }

    val context = LocalContext.current

    val isFormValid = productName.isNotBlank() && description.isNotBlank() && price.isNotBlank() && stock.isNotBlank() && imageFile != null

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // Convertir URI en archivo
            val file = uriToFile(it, context)
            imageFile = file
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = providerId,
            onValueChange = { providerId = it },
            label = { Text("ProviderId") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Image")
        }

        imageFile?.let { file ->
            Text("Selected file: ${file.name}", modifier = Modifier.padding(top = 8.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBackClick
            ) {
                Text("Back")
            }
            Button(
                onClick = {
                    // Asegúrate de que los valores estén bien convertidos antes de pasarlos al ViewModel
                    val priceDouble = price.toDoubleOrNull() ?: 0.0  // Si no es un número válido, usa 0.0
                    val stockInt = stock.toIntOrNull() ?: 0 // Si no es un número válido, usa 0

                    // Verificar si el archivo está seleccionado y proceder a enviar el producto
                    imageFile?.let {
                        viewModel.addProduct(
                            productName = productName,
                            description = description,
                            price = priceDouble,
                            stock = stockInt,
                            providerId = providerId,
                            imageFile = it  // Enviar el archivo
                        )
                    }
                },
                enabled = isFormValid
            ) {
                Text("Add Product")
            }
        }
    }
}

fun uriToFile(uri: Uri, context: Context): File {
    // Crear un archivo temporal para almacenar la imagen
    val contentResolver: ContentResolver = context.contentResolver
    val tempFile = File(context.cacheDir, "product_image.png")
    try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        outputStream.close()
        inputStream?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return tempFile
}


fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}