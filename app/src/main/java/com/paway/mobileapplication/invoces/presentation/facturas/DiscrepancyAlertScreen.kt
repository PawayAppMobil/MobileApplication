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
fun DiscrepancyAlertScreen(viewModel: DiscrepancyAlertViewModel) {
    val alerts by viewModel.alerts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderD()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "HISTORIAL DE ALERTAS",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF006064)
        )
        Spacer(modifier = Modifier.height(16.dp))
        AlertList(alerts)
        Spacer(modifier = Modifier.height(16.dp))
        ActionButtons(viewModel)
    }
}

@Composable
fun HeaderD() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "DISCREPANCY ALERTS",
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
fun AlertList(alerts: List<Alert>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AlertListHeader()
            LazyColumn {
                items(alerts) { alert ->
                    AlertItem(alert)
                }
            }
        }
    }
}

@Composable
fun AlertListHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AlertHeaderItem("N° Factura", Modifier.weight(1f))
        AlertHeaderItem("Proveedor", Modifier.weight(1f))
        AlertHeaderItem("Fecha", Modifier.weight(1f))
        AlertHeaderItem("Tipo de Discrepancia", Modifier.weight(1f))
        AlertHeaderItem("Estado", Modifier.weight(1f))
    }
}

@Composable
fun AlertHeaderItem(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color.Blue,
        modifier = modifier.padding(4.dp)
    )
}

@Composable
fun AlertItem(alert: Alert) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AlertItemText(alert.invoiceNumber, Modifier.weight(1f))
        AlertItemText(alert.provider, Modifier.weight(1f))
        AlertItemText(alert.date, Modifier.weight(1f))
        AlertItemText(alert.discrepancyType, Modifier.weight(1f))
        AlertItemText(alert.status, Modifier.weight(1f))
    }
}

@Composable
fun AlertItemText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.Blue,
        modifier = modifier.padding(4.dp)
    )
}

@Composable
fun ActionButtons(viewModel: DiscrepancyAlertViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButtonC("Exportar a CSV", Modifier.fillMaxWidth()) { viewModel.exportToCSV() }
        ActionButtonC("Cancelar", Modifier.fillMaxWidth()) { viewModel.cancel() }
    }
}

@Composable
fun ActionButtonC(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color.White)
    }
}

data class Alert(
    val invoiceNumber: String,
    val provider: String,
    val date: String,
    val discrepancyType: String,
    val status: String
)

class DiscrepancyAlertViewModel {
    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts

    init {
        // Populate with sample data
        _alerts.value = listOf(
            Alert("12345", "A", "01/09/2024", "Monto Incorrecto", "Activo"),
            Alert("12347", "B", "05/09/2024", "Monto Incorrecto", "Resuelto"),
            Alert("12349", "A", "10/08/2024", "Fecha Vencida", "Resuelto")
        )
    }

    fun exportToCSV() {
    }

    fun cancel() {
    }
}