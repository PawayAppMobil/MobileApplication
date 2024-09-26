package com.paway.mobileapplication.inventory.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.Greeting
import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.remote.InvoiceService
import com.paway.mobileapplication.inventory.data.repository.InvoiceRepository
import com.paway.mobileapplication.inventory.domain.Invoice
import com.paway.mobileapplication.ui.theme.MobileApplicationTheme

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun InvoiceListScreen(viewModel: InvoiceListViewModel, modifier: Modifier = Modifier) {

    val state = viewModel.state.value
    LaunchedEffect(Unit) {
        viewModel.onStatusChanged("pagar") // Puedes cambiar "pagado" por "pagar" según sea necesario
    }
    Scaffold(modifier = modifier) { paddingValues: PaddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
            if (state.error.isNotEmpty()) {
                Text(state.error)
            }
            state.data?.let { invoices: List<Invoice> ->
                LazyColumn {
                    items(invoices) { invoice ->
                        Card(modifier = Modifier.padding(4.dp)) {
                            Column {
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = "Invoice ID: ${invoice.id}",
                                    fontSize = 20.sp
                                )
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = "Amount: ${invoice.amount}"
                                )
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = "Status: ${invoice.status}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InvoiceListScreenPreview() {
    val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(InvoiceService::class.java)
    val repository = InvoiceRepository(service)
    val viewModel = InvoiceListViewModel(repository)
    InvoiceListScreen(viewModel)
}