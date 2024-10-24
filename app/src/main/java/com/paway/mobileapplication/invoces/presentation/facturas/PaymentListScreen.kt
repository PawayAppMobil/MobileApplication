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
fun PaymentListScreen(viewModel: PaymentListViewModel) {
    val payments by viewModel.payments.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderC()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "LISTA DE PAGOS PROGRAMADOS",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF006064)
        )
        Spacer(modifier = Modifier.height(16.dp))
        PaymentList(payments)
        Spacer(modifier = Modifier.height(16.dp))
        ActionButtons(viewModel)
    }
}

@Composable
fun HeaderC() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "LIST OF PAYMENTS",
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
fun PaymentList(payments: List<Payment>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PaymentListHeader()
            LazyColumn {
                items(payments) { payment ->
                    PaymentItem(payment)
                }
            }
        }
    }
}

@Composable
fun PaymentListHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PaymentHeaderItem("N° Pago", Modifier.weight(1f))
        PaymentHeaderItem("Fecha de ejecución", Modifier.weight(1f))
        PaymentHeaderItem("Monto", Modifier.weight(1f))
        PaymentHeaderItem("Estado", Modifier.weight(1f))
    }
}

@Composable
fun PaymentHeaderItem(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color.Blue,
        modifier = modifier.padding(4.dp)
    )
}

@Composable
fun PaymentItem(payment: Payment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PaymentItemText(payment.number, Modifier.weight(1f))
        PaymentItemText(payment.date, Modifier.weight(1f))
        PaymentItemText(payment.amount, Modifier.weight(1f))
        PaymentItemText(payment.status, Modifier.weight(1f))
    }
}

@Composable
fun PaymentItemText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.Blue,
        modifier = modifier.padding(4.dp)
    )
}

@Composable
fun ActionButtons(viewModel: PaymentListViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionButtonB("Editar", Modifier.weight(1f)) { viewModel.editPayments() }
        Spacer(modifier = Modifier.width(8.dp))
        ActionButtonB("Cancelar", Modifier.weight(1f)) { viewModel.cancelPayments() }
        Spacer(modifier = Modifier.width(8.dp))
        ActionButtonB("Exportar a CVS", Modifier.weight(1f)) { viewModel.exportToCSV() }
    }
}

@Composable
fun ActionButtonB(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color.White)
    }
}

data class Payment(val number: String, val date: String, val amount: String, val status: String)

class PaymentListViewModel {
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments

    init {
        _payments.value = listOf(
            Payment("0001", "10/09/2024", "500 USD", "Programado"),
            Payment("0002", "15/09/2024", "1000 USD", "Pendiente")
        )
    }

    fun editPayments() {
    }

    fun cancelPayments() {
    }

    fun exportToCSV() {
    }
}