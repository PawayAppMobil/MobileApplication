package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.invoces.data.repository.WebServiceRepository
import com.paway.mobileapplication.inventory.common.RetrofitClient
@Composable
fun InvoiceDashboardScreen(dashboardViewModel: InvoiceDashboardViewModel, userId: String?) {
    var currentScreen by remember { mutableStateOf("dashboard") }

    LaunchedEffect(userId) {
        userId?.let { dashboardViewModel.setUserId(it) }
    }

    val state = dashboardViewModel.state.value

    val importInvoiceViewModel = ImportInvoiceViewModel(WebServiceRepository(RetrofitClient.webService))
    val invoiceListViewModel = InvoiceListViewModel(WebServiceRepository(RetrofitClient.webService))
    val schedulePaymentsViewModel = SchedulePaymentsViewModel()
    val paymentListViewModel = PaymentListViewModel()
    val discrepancyAlertViewModel = DiscrepancyAlertViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Header()
        Spacer(modifier = Modifier.height(16.dp))

        when (currentScreen) {
            "importarFacturas" -> ImportInvoiceScreen(importInvoiceViewModel, userId)
            "verFacturas" -> InvoiceListScreen(invoiceListViewModel, userId)
            "programarPagos" -> SchedulePaymentsScreen(schedulePaymentsViewModel)
            "historialPagos" -> PaymentListScreen(paymentListViewModel)
            "alertas" -> DiscrepancyAlertScreen(discrepancyAlertViewModel)
            else -> {
                DashboardButton()
                Spacer(modifier = Modifier.height(16.dp))
                PendingInvoicesSection(state, dashboardViewModel)
                Spacer(modifier = Modifier.height(48.dp))
                ActionButtons(onButtonClick = { screen -> currentScreen = screen })
            }
        }
    }
}
@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SUPPLIER INVOICE INFO",
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
fun DashboardButton() {
    Button(
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("DASHBOARD", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PendingInvoicesSection(state: DashboardUIState, viewModel: InvoiceDashboardViewModel) {
    var showDaysDialog by remember { mutableStateOf(false) }
    var daysInput by remember { mutableStateOf(state.daysToCheck.toString()) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RESUMEN DE FACTURAS",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF006064),
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Card(
                    modifier = Modifier
                        .width(140.dp)
                        .height(60.dp)
                        .clickable { showDaysDialog = true },
                    colors = CardDefaults.cardColors(containerColor = Color.Blue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Facturas por\nvencer (${state.daysToCheck} días)",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                InfoCard("Facturas\nVencidas", alignment = Alignment.Center)
                InfoCard("Total de\nFacturas", alignment = Alignment.Center)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                InfoCard(state.totalPendingInvoices, alignment = Alignment.Center)
                InfoCard(state.totalOverdueInvoices, alignment = Alignment.Center)
                InfoCard(state.totalInvoices, alignment = Alignment.Center)
            }
        }

        if (showDaysDialog) {
            AlertDialog(
                onDismissRequest = { showDaysDialog = false },
                title = { Text("Configurar días a vencer") },
                text = {
                    OutlinedTextField(
                        value = daysInput,
                        onValueChange = { daysInput = it.filter { char -> char.isDigit() } },
                        label = { Text("Días") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        daysInput.toIntOrNull()?.let { days ->
                            if (days > 0) {
                                viewModel.updateDaysToCheck(days)
                            }
                        }
                        showDaysDialog = false
                    }) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDaysDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error?.let { error ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun InfoCard(text: String, alignment: Alignment) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(60.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Blue),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = alignment
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActionButtons(onButtonClick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionButton("IMPORTAR FACTURAS", Modifier.weight(1f)) {
                onButtonClick("importarFacturas")
            }
            Spacer(modifier = Modifier.width(8.dp))
            ActionButton("VER FACTURAS", Modifier.weight(1f)) {
                onButtonClick("verFacturas")
            }
        }
        ActionButton("PROGRAMAR PAGOS", Modifier.fillMaxWidth()) {
            onButtonClick("programarPagos")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionButton("HISTORIAL DE PAGOS", Modifier.weight(1f)) {
                onButtonClick("historialPagos")
            }
            Spacer(modifier = Modifier.width(8.dp))
            ActionButton("ALERTAS", Modifier.weight(1f)) {
                onButtonClick("alertas")
            }
        }
    }
}
@Composable
fun ActionButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF012E2E)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}



