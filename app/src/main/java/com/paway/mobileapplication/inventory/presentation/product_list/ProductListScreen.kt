package com.paway.mobileapplication.inventory.presentation.product_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paway.mobileapplication.inventory.domain.Product

@Composable
fun MainScreen(viewModel: ProductListViewModel) {
    InventoryScreen(viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(viewModel: ProductListViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("INVENTARIO") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF006C67)
                ),
                actions = {
                    IconButton(onClick = { /* TODO: Implement notification action */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { /* TODO: Implement more options action */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(innerPadding)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                modifier = Modifier.padding(16.dp)
            )

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(state.products) { product ->
                    ProductItem(product)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        placeholder = { Text("Buscar...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = { 
            IconButton(onClick = { /* TODO: Implement filter action */ }) {
                Icon(Icons.Default.List, contentDescription = "Filter")
            }
        },
        // Remove the colors parameter to use default colors
        singleLine = true
    )
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (product.id.hashCode() % 2 == 0) Color(0xFFE0F2F1) else Color(0xFFFFF9C4)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Nombre: ${product.name}",
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Stock actual: ${product.stock} unidades")
            }
            IconButton(onClick = { /* TODO: Implement favorite action */ }) {
                Icon(Icons.Default.Star, contentDescription = "Favorite")
            }
        }
    }
}