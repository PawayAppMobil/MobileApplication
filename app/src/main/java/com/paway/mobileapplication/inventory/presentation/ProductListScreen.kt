package com.paway.mobileapplication.inventory.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.paway.mobileapplication.inventory.domain.Product

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (String) -> Unit
) {
    val state = viewModel.state.value
    val name = viewModel.name.value

    val buttonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Color.White,
        containerColor = Color(0xff005555)
    )


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
                .background(Color(0xffd9d9d9)),
            horizontalAlignment = Alignment.CenterHorizontally,


            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = { viewModel.filterProductsByName() },
                    colors = buttonColors) {
                    Text("Filter by Name")
                }
                OutlinedButton(onClick = { viewModel.filterProductsByStock() },
                    colors = buttonColors
                ) {
                    Text("Filter by Stock")
                }
                OutlinedButton(onClick = { viewModel.filterProductsByFavorites() },
                    colors = buttonColors

                ) {
                    Text("Filter by Favorites")
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
            },colors = buttonColors


            ) { Text("Search") }
            state.data?.let { products: List<Product> ->
                LazyColumn {
                    itemsIndexed(products) { index, product ->
                        val backgroundColor = if (index % 2 == 0) Color(0xfffce199) else Color(0xff7faaaa)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable { onProductClick(product.id) }
                        ) {
                            ProductItem(product, backgroundColor) {
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
fun ProductItem(product: Product,backgroundColor:Color ,onFavoritePressed: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),

        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RectangleShape


    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
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
                    tint = if (product.isFavorite) Color.DarkGray else Color.LightGray
                )
            }
        }

    }

}