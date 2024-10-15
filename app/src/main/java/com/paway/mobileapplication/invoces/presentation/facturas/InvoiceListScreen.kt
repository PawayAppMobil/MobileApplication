package com.paway.mobileapplication.invoces.presentation.facturas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun InvoiceListScreen(viewModel: InvoiceListViewModel) {
    val invoices by viewModel.invoices.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderA()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "FACTURAS IMPORTADAS",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF006064)
        )
        Spacer(modifier = Modifier.height(16.dp))
        InvoiceList(invoices)
        Spacer(modifier = Modifier.height(16.dp))
        ActionButtons(viewModel)
    }
}

@Composable
fun HeaderA() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "INVOICE LIST",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF006064)
        )
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color(0xFF006064)
        )
    }
}

@Composable
fun InvoiceList(invoices: List<Invoice>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            items(invoices) { invoice ->
                InvoiceItem(invoice)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun InvoiceItem(invoice: Invoice) {
    Button(
        onClick = { /* Handle invoice selection */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "Factura #${invoice.number}: S/${invoice.amount} - ${invoice.status}",
            color = Color.White
        )
    }
}

@Composable
fun ActionButtons(viewModel: InvoiceListViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButton("Revisar Detalles") { viewModel.reviewDetails() }
        ActionButton("Confirmar") { viewModel.confirmInvoices() }
        ActionButton("Descartar") { viewModel.discardInvoices() }
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color.White)
    }
}

data class Invoice(val number: String, val amount: Int, val status: String)

class InvoiceListViewModel {
    private val _invoices = MutableStateFlow<List<Invoice>>(emptyList())
    val invoices: StateFlow<List<Invoice>> = _invoices

    init {
        // Populate with sample data
        _invoices.value = listOf(
            Invoice("1232", 500, "Pendiente"),
            Invoice("1233", 1000, "Pendiente"),
            Invoice("1234", 3000, "Pendiente")
        )
    }

    fun reviewDetails() {
        // Implement review details logic
    }

    fun confirmInvoices() {
        // Implement confirm invoices logic
    }

    fun discardInvoices() {
        // Implement discard invoices logic
    }
}
