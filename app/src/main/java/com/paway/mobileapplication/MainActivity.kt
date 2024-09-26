package com.paway.mobileapplication



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.local.AppDataBase
import com.paway.mobileapplication.inventory.data.remote.InvoiceService
import com.paway.mobileapplication.inventory.data.remote.ProductService
import com.paway.mobileapplication.inventory.data.repository.InvoiceRepository
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.GetProductByIdUseCase
import com.paway.mobileapplication.inventory.presentation.InvoiceListScreen
import com.paway.mobileapplication.inventory.presentation.InvoiceListViewModel
import com.paway.mobileapplication.inventory.presentation.ProductDetailScreen
import com.paway.mobileapplication.inventory.presentation.ProductDetailViewModel
import com.paway.mobileapplication.inventory.presentation.ProductListScreen
import com.paway.mobileapplication.inventory.presentation.ProductListViewModel
import com.paway.mobileapplication.ui.theme.MobileApplicationTheme

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivitySelector : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileApplicationTheme {
                MainActivitySelectorContent()
            }
        }
    }
}

@Composable
fun MainActivitySelectorContent() {
    val (selectedActivity, setSelectedActivity) = remember { mutableStateOf(1) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { setSelectedActivity(1) }) {
            Text("Home")
        }
        Button(onClick = { setSelectedActivity(2) }) {
            Text("Inventario")
        }

        when (selectedActivity) {
            1 -> MainActivity1Content()
            2 -> MainActivity2Content()
        }
    }
}

@Composable
fun MainActivity1Content() {
    val context = LocalContext.current
    val database = Room.databaseBuilder(
        context,
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

    AppContent(productListViewModel, productDetailViewModel)
}

@Composable
fun MainActivity2Content() {
    val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(InvoiceService::class.java)
    val repository = InvoiceRepository(service)
    val viewModel = InvoiceListViewModel(repository)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        InvoiceListScreen(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AppContent(
    productListViewModel: ProductListViewModel,
    productDetailViewModel: ProductDetailViewModel
) {
    val (currentScreen, setCurrentScreen) = remember { mutableStateOf<Screen>(Screen.ProductList) }

    when (currentScreen) {
        is Screen.ProductList -> {
            ProductListScreen(
                viewModel = productListViewModel,
                onProductClick = { productId ->
                    productDetailViewModel.getProductById(productId)
                    setCurrentScreen(Screen.ProductDetail(productId))
                }
            )
        }
        is Screen.ProductDetail -> {
            ProductDetailScreen(
                viewModel = productDetailViewModel,
                productId = (currentScreen as Screen.ProductDetail).productId,
                onBackClick = {
                    setCurrentScreen(Screen.ProductList)
                }
            )
        }
    }
}

sealed class Screen {
    object ProductList : Screen()
    data class ProductDetail(val productId: String) : Screen()
}

@Preview(showBackground = true)
@Composable
fun MainActivitySelectorPreview() {
    MobileApplicationTheme {
        MainActivitySelectorContent()
    }
}