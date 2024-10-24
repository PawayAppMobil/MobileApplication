package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.foundation.layout.*
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

@Composable
fun SchedulePaymentsScreen(viewModel: SchedulePaymentsViewModel) {
    var selectedInvoices by remember { mutableStateOf("") }
    var executionDate by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderB()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "PROGRAMAR PAGOS",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF006064)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PaymentField("Selecciona Facturas", selectedInvoices) {
                    // Implement invoice selection logic
                }
                PaymentField("Fecha de Ejecución", executionDate) {
                    // Implement date selection logic
                }
                PaymentField("Monto", amount) {
                    // Implement amount input logic
                }
                ActionButtonA("Guardar y Programar Pagos") {
                    viewModel.schedulePayments(selectedInvoices, executionDate, amount)
                }
                ActionButtonA("Cancelar") {
                    viewModel.cancel()
                }
            }
        }
    }
}

@Composable
fun HeaderB() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SCHEDULE PAYMENTS",
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
fun PaymentField(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { /* Implement field-specific action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(label, color = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onValueChange(value) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(if (value.isEmpty()) label else value, color = Color.White)
        }
    }
}

@Composable
fun ActionButtonA(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color.White)
    }
}

class SchedulePaymentsViewModel {
    fun schedulePayments(invoices: String, date: String, amount: String) {
    }

    fun cancel() {
    }
}