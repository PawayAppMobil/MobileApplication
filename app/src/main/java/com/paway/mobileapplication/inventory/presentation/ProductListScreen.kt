package com.paway.mobileapplication.inventory.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.paway.mobileapplication.inventory.domain.Product
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (String) -> Unit,
    onAddProductClick: () -> Unit,
) {
    val state = viewModel.state.value
    val name = viewModel.name.value
    var expanded by remember { mutableStateOf(false) }

    val buttonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Color.White,
        containerColor = Color(0xff005555)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xffd9d9d9)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    IconButton(onClick = {expanded = !expanded}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.DarkGray)
                    }
                    AnimatedVisibility(visible = expanded) {
                        Column {
                            OutlinedButton(onClick = { viewModel.filterProductsByName() }, colors = buttonColors) {
                                Text("Order by Name")
                            }
                            OutlinedButton(onClick = { viewModel.filterProductsByStock() }, colors = buttonColors) {
                                Text("Order by Stock")
                            }
                            OutlinedButton(onClick = {  }, colors = buttonColors) {
                                Text("Order by shortages")
                            }
                        }
                    }
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(Color(0xffb2cccc)),
                value = name,
                onValueChange = {
                    viewModel.onNameChanged(it)
                })
            OutlinedButton(onClick = {
                viewModel.searchProduct()
            }, colors = buttonColors) { Text("Search") }
            state.data?.let { products: List<Product> ->
                LazyColumn {
                    itemsIndexed(products) { index, product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable { onProductClick(product.id) },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF839BC7))
                        ) {
                            ProductItem(product, Color(0xFF829AC6)) {
                                viewModel.deleteProduct(product)
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
                if (state.error.isNotEmpty()) {
                    Text("There isn't products")
                }
            }
        }
    }
}


@Composable
fun ProductItem(product: Product, backgroundColor: Color, onDeletePressed: () -> Unit) {
    val iconColor = if (product.stock < product.initialStock) Color.Yellow else Color.Green
    val imageBitmap = base64ToImageBitmap(product.image)
    var isHovered by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RectangleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(if (isHovered) Color.Red else backgroundColor)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isHovered = true
                            tryAwaitRelease()
                            isHovered = false
                        }
                    )
                }
        ) {
            IconButton(onClick = onDeletePressed) {
                Icon(Icons.Default.Clear, contentDescription = "Delete", tint = Color.Red)
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(3f)
            ) {
                Text(product.productName)
                Text(product.stock.toString())
                imageBitmap?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "Product image",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            Icon(
                Icons.Filled.Info,
                contentDescription = "Info",
                tint = iconColor
            )
        }
    }
}

fun base64ToImageBitmap(base64: String): ImageBitmap? {
    return try {
        val decodedString = Base64.decode(base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        bitmap.asImageBitmap()
    } catch (e: IllegalArgumentException) {
        null
    }
}