package com.paway.mobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.paway.mobileapplication.inventory.presentation.product_list.ProductListViewModel // Ensure this is the correct import
import com.paway.mobileapplication.ui.theme.MobileApplicationTheme
import com.paway.mobileapplication.data.remote.RetrofitInstance
import com.paway.mobileapplication.inventory.data.repository.ProductRepositoryImpl
import com.paway.mobileapplication.inventory.domain.use_case.SearchProductsUseCase
import com.paway.mobileapplication.inventory.presentation.product_list.MainScreen
class MainActivity : ComponentActivity() {
    private lateinit var searchProductsUseCase: SearchProductsUseCase
    private val productListViewModel: ProductListViewModel by viewModels {
        ProductListViewModel.provideFactory(searchProductsUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar Retrofit y crear la instancia de SearchProductsUseCase
        val apiService = RetrofitInstance.api
        val repository = ProductRepositoryImpl(apiService)
        searchProductsUseCase = SearchProductsUseCase(repository)
        
        enableEdgeToEdge()
        setContent {
            MobileApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainScreen(
                            viewModel = productListViewModel
                        )
                    }
                }
            }
        }
    }


}


