package com.paway.mobileapplication.inventory.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    productId: String,
    onBackClick: () -> Unit
) {
    val hasFetched = remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        if (!hasFetched.value) {
            Log.d("ProductDetailScreen", "LaunchedEffect with productId: $productId")
            viewModel.getProductById(productId)
            hasFetched.value = true
        }
    }

    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.error.isNotEmpty() -> {
                    Text(state.error, color = Color.Red)
                }
                else -> {
                    val product = state.data
                    if (product != null) {
                        Text("Product ID: ${product.id}")
                        Text("Product Name: ${product.name}")
                        Text("Stock: ${product.stock}")
                        // Add other product details here
                    } else {
                        Text("No product data available")
                    }
                }
            }
        }
    }
}