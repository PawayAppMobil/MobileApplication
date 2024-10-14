import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.invoces.presentation.facturas.DashboardUIState
import com.paway.mobileapplication.invoces.presentation.facturas.InvoiceDashboardViewModel

@Composable
fun InvoiceDashboardScreen(viewModel: InvoiceDashboardViewModel, userId: String?) {
    // Controla la pantalla actual que se va a mostrar
    var currentScreen by remember { mutableStateOf("dashboard") }

    LaunchedEffect(userId) {
        userId?.let { viewModel.setUserId(it) }
    }

    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Header()
        Spacer(modifier = Modifier.height(16.dp))

        // Cambia el contenido de la pantalla según el estado actual
        when (currentScreen) {
            "importarFacturas" -> ImportInvoiceScreen()
            "verFacturas" -> ViewInvoicesScreen()
            "programarPagos" -> ProgramPaymentsScreen()
            "historialPagos" -> PaymentHistoryScreen()
            "alertas" -> AlertsScreen()
            else -> {
                DashboardButton()
                Spacer(modifier = Modifier.height(16.dp))
                PendingInvoicesSection(state)
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
fun PendingInvoicesSection(state: DashboardUIState) {
    Text(
        text = "PENDING INVOICES",
        fontWeight = FontWeight.Bold,
        color = Color(0xFF006064),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
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
            InfoCard("Total facturas\npendientes", alignment = Alignment.Center)
            InfoCard("Total Pagos\nProgramados", alignment = Alignment.Center)
            InfoCard("Alertas", alignment = Alignment.Center)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            InfoCard(state.totalPendingInvoices, alignment = Alignment.Center)
            InfoCard(state.totalScheduledPayments, alignment = Alignment.Center)
            InfoCard(state.totalAlerts, alignment = Alignment.Center)
        }
    }

    if (state.isLoading) {
        CircularProgressIndicator()
    }

    state.error?.let { error ->
        Text(
            text = error,
            color = Color.Red,
        )
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

@Composable
fun ImportInvoiceScreen() {
com.paway.mobileapplication.invoces.presentation.facturas.ImportInvoiceScreen()
}

@Composable
fun ViewInvoicesScreen() {
    Text("Pantalla de Ver Facturas")
}

@Composable
fun ProgramPaymentsScreen() {
    Text("Pantalla de Programar Pagos")
}

@Composable
fun PaymentHistoryScreen() {
    Text("Pantalla de Historial de Pagos")
}

@Composable
fun AlertsScreen() {
    Text("Pantalla de Alertas")
}
