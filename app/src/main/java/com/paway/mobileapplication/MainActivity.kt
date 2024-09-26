package com.paway.mobileapplication



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.remote.InvoiceService
import com.paway.mobileapplication.inventory.data.repository.InvoiceRepository
import com.paway.mobileapplication.inventory.presentation.InvoiceListScreen
import com.paway.mobileapplication.inventory.presentation.InvoiceListViewModel
import com.paway.mobileapplication.ui.theme.MobileApplicationTheme

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(InvoiceService::class.java)
        val repository = InvoiceRepository(service)
        val viewModel = InvoiceListViewModel(repository)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InvoiceListScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileApplicationTheme {
        Greeting("Android")
    }
}
