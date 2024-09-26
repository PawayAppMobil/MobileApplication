package com.paway.mobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.local.AppDataBase
import com.paway.mobileapplication.inventory.data.remote.ProductService
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.GetProductByIdUseCase
import com.paway.mobileapplication.inventory.domain.Product
import com.paway.mobileapplication.inventory.presentation.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "product-database"
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productService = retrofit.create(ProductService::class.java)
        val productDao = database.getProductDao()
        val productRepository = ProductRepository(productService, productDao)
        val getProductByIdUseCase = GetProductByIdUseCase(productRepository)

        val productListViewModel = ProductListViewModel(productRepository)
        val productDetailViewModel = ProductDetailViewModel(getProductByIdUseCase)
        val productHistoryViewModel = ProductHistoryViewModel(productRepository)

        setContent {
            AppContent(productListViewModel, productDetailViewModel, productHistoryViewModel, productRepository)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(
    productListViewModel: ProductListViewModel,
    productDetailViewModel: ProductDetailViewModel,
    productHistoryViewModel: ProductHistoryViewModel,
    productRepository: ProductRepository
) {
    var selectedTab by remember { mutableStateOf(0) }
    var currentProductId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Products") },
                    label = { Text("Products") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "History") },
                    label = { Text("History") }
                )
            }
        }
    ) { paddingValues ->
        when {
            currentProductId != null -> {
                ProductDetailScreen(
                    viewModel = productDetailViewModel,
                    productId = currentProductId!!,
                    onBackClick = { currentProductId = null }
                )
            }
            selectedTab == 0 -> {
                ProductListScreen(
                    viewModel = productListViewModel,
                    onProductClick = { productId ->
                        currentProductId = productId
                        productListViewModel.viewModelScope.launch {
                            val product = productRepository.getProductFromLocal(productId)
                            if (product != null) {
                                productRepository.addToHistory(product)
                            } else {
                                // Si no está en la base de datos local, usamos un producto simulado
                                val simulatedProduct = Product(
                                    id = productId,
                                    name = "Unknown Product",
                                    stock = 0,
                                    isFavorite = false
                                )
                                productRepository.addToHistory(simulatedProduct)
                            }
                        }
                    }
                )
            }
            selectedTab == 1 -> {
                ProductHistoryScreen(
                    viewModel = productHistoryViewModel,
                    onProductClick = { productId ->
                        currentProductId = productId
                    }
                )
            }
        }
    }
}

