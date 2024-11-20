package com.paway.mobileapplication.inventory.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream
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
    var imageBase64 by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    val isFormValid = productName.isNotBlank() && description.isNotBlank() && price.isNotBlank() && stock.isNotBlank() && imageBase64.isNotBlank()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageBitmap = bitmap
            imageBase64 = bitmapToBase64(bitmap)
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
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
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
                    viewModel.addProduct(productName, description, price.toDouble(), stock.toInt(), providerId, imageBase64, onProductAdded)
                },
                enabled = isFormValid
            ) {
                Text("Add Product")
            }
        }
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}