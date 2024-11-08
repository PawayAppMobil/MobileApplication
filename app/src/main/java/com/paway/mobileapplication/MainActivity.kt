package com.paway.mobileapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.paway.mobileapplication.R
import ReportScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.paway.mobileapplication.common.Constants

import com.paway.mobileapplication.common.RetrofitClient
import com.paway.mobileapplication.inventory.data.local.AppDataBase
import com.paway.mobileapplication.inventory.data.remote.ProductService
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.GetProductByIdUseCase
import com.paway.mobileapplication.inventory.presentation.ProductDetailScreen
import com.paway.mobileapplication.inventory.presentation.ProductDetailViewModel
import com.paway.mobileapplication.inventory.presentation.ProductListScreen
import com.paway.mobileapplication.inventory.presentation.ProductListViewModel
import com.paway.mobileapplication.invoces.data.repository.WebServiceRepository
import com.paway.mobileapplication.invoces.presentation.facturas.InvoiceDashboardScreen
import com.paway.mobileapplication.invoces.presentation.facturas.InvoiceDashboardViewModel

import com.paway.mobileapplication.reports.data.repository.report.ReportRepository
import com.paway.mobileapplication.reports.presentation.reports.ReportViewModel
import com.paway.mobileapplication.ui.theme.MobileApplicationTheme
import com.paway.mobileapplication.user.data.repository.HomeRepository
import com.paway.mobileapplication.user.presentation.HomeScreenContent
import com.paway.mobileapplication.user.presentation.HomeViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val InvoiceDashboardviewModel = InvoiceDashboardViewModel(WebServiceRepository(RetrofitClient.webService))
val ReportViewModel = ReportViewModel(ReportRepository(RetrofitClient.reportService))
val HomeViewModel = HomeViewModel(HomeRepository(RetrofitClient.homeServiceApi))
class MainActivitySelector : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getStringExtra("USER_ID")

        setContent {
            MobileApplicationTheme {
                MainScreen(userId) // Pasar el userId a MainScreen
            }
        }
    }
}


@Composable
fun MainScreen(userId: String?) {
    var selectedScreen by remember { mutableStateOf("home") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f)
        ) {
            when (selectedScreen) {
                "home" -> HomeScreen(homeViewModel = HomeViewModel)
                "inventory" -> InventoryScreen(userId)
                "invoice" -> InvoiceScreen(userId)
                "balance" -> BalanceScreen()
            }
        }

        BottomNavigationBar(selectedScreen) { screen ->
            selectedScreen = screen
        }
    }
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    HomeScreenContent(homeViewModel = homeViewModel)
}
@Composable
fun InventoryScreen(userId: String?) {
    val context = LocalContext.current
    val database = Room.databaseBuilder(
        context,
        AppDataBase::class.java, "product-database"
    ).build()

    val retrofit = Retrofit.Builder()
        .baseUrl(com.paway.mobileapplication.inventory.common.Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productService = retrofit.create(ProductService::class.java)
    val productDao = database.getProductDao()
    val productRepository = ProductRepository(productService, productDao)
    val getProductByIdUseCase = GetProductByIdUseCase(productRepository)

    val productListViewModel = ProductListViewModel(productRepository,userId ?: "")
    val productDetailViewModel = ProductDetailViewModel(getProductByIdUseCase)

    AppContent(productListViewModel, productDetailViewModel)
}


@Composable
fun InvoiceScreen(userId: String?) {
    InvoiceDashboardScreen(
        InvoiceDashboardviewModel,
        userId = userId ?: ""
    )
}

@Composable
fun BalanceScreen() {
    ReportScreen(ReportViewModel)
}
@Composable
fun BottomNavigationBar(selectedScreen: String, onItemSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem(
            icon = Icons.Default.Home,
            label = "Inicio",
            isSelected = selectedScreen == "home",
            onClick = { onItemSelected("home") }
        )
        BottomNavItem(
            icon = Icons.Default.List,
            label = "Inventario",
            isSelected = selectedScreen == "inventory",
            onClick = { onItemSelected("inventory") }
        )
        BottomNavItem(
            icon = Icons.Default.Add,
            label = "Generar Factura",
            isSelected = selectedScreen == "invoice",
            onClick = { onItemSelected("invoice") }
        )
        BottomNavItem(
            icon = Icons.Default.DateRange,
            label = "Balance",
            isSelected = selectedScreen == "balance",
            onClick = { onItemSelected("balance") }
        )
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.Yellow else Color.Gray
        )
        Text(
            text = label,
            color = if (isSelected) Color.Yellow else Color.Gray
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
fun MainScreenPreview() {
    MobileApplicationTheme {
        MainScreen(userId = "Test User")
    }
}