package com.paway.mobileapplication.inventory.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paway.mobileapplication.inventory.domain.Product

@Composable
fun ProductListScreen(viewModel: ProductListViewModel) {
    val state = viewModel.state.value
    val name = viewModel.name.value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = { viewModel.filterProductsByName() }) {
                    Text("Filter by Name")
                }
                OutlinedButton(onClick = { viewModel.filterProductsByStock() }) {
                    Text("Filter by Stock")
                }
                OutlinedButton(onClick = { viewModel.filterProductsByFavorites() }) {
                    Text("Filter by Favorites")
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                value = name,
                onValueChange = {
                    viewModel.onNameChanged(it)
                })
            OutlinedButton(onClick = {
                viewModel.searchProduct()
            }) { Text("Search") }
            state.data?.let { products: List<Product> ->
                LazyColumn {
                    items(products) { product: Product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            ProductItem(product) {
                                viewModel.toggleFavorite(product)
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
                    Text(state.error)
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onFavoritePressed: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(3f)
        ) {
            Text(product.name)
            Text(product.stock.toString())
        }

        IconButton(onClick = {
            onFavoritePressed()
        }) {
            Icon(
                Icons.Filled.Star,
                "Favorite",
                tint = if (product.isFavorite) Color.Red else Color.Gray
            )
        }
    }
}