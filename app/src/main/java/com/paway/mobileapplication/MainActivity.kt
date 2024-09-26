package com.paway.mobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.local.AppDataBase
import com.paway.mobileapplication.inventory.data.remote.ProductService
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.presentation.ProductDetailScreen
import com.paway.mobileapplication.inventory.presentation.ProductDetailViewModel
import com.paway.mobileapplication.inventory.presentation.ProductListScreen
import com.paway.mobileapplication.inventory.presentation.ProductListViewModel
import com.paway.mobileapplication.ui.theme.MobileApplicationTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductService::class.java)

        val dao = Room
            .databaseBuilder(applicationContext, AppDataBase::class.java, "products-db")
            .build()
            .getProductDao()

        val viewModel = ProductListViewModel(ProductRepository(service, dao))

        enableEdgeToEdge()
        setContent {
            MobileApplicationTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController, productListViewModel = viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavController, productListViewModel: ProductListViewModel) {
    // Asegúrate de que navController sea un NavHostController
    NavHost(navController = navController as NavHostController, startDestination = "product_list") {
        composable("product_list") {
            ProductListScreen(
                viewModel = productListViewModel,
                onProductClicked = { product ->
                    // Navegamos a la pantalla de detalles con el ID del producto
                    navController.navigate("product_detail/${product.id}")
                }
            )
        }

        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""

            // Buscamos el producto en la lista del ViewModel
            val selectedProduct = productListViewModel.state.value.data?.find { it.id == productId }

            if (selectedProduct != null) {
                // Crear el ViewModel de detalle y pasar el producto
                val detailViewModel: ProductDetailViewModel = viewModel()
                detailViewModel.setProduct(selectedProduct)  // Aquí llamamos a setProduct con el producto seleccionado

                ProductDetailScreen(
                    viewModel = detailViewModel,
                    onSave = {
                        // Guardar los cambios y regresar
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            } else {
                // Mostrar mensaje de error si no se encontró el producto
                Text("Producto no encontrado.")
            }
        }
    }
}
