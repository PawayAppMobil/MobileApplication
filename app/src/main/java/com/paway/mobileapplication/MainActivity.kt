package com.paway.mobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.room.Room
import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.local.AppDataBase
import com.paway.mobileapplication.inventory.data.remote.ProductService
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.GetProductByIdUseCase
import com.paway.mobileapplication.inventory.presentation.ProductDetailScreen
import com.paway.mobileapplication.inventory.presentation.ProductDetailViewModel
import com.paway.mobileapplication.inventory.presentation.ProductListScreen
import com.paway.mobileapplication.inventory.presentation.ProductListViewModel
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

        setContent {
            AppContent(productListViewModel, productDetailViewModel)
        }
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

