import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paway.mobileapplication.data.remote.dto.report.TransactionDto
import com.paway.mobileapplication.domain.model.report.Transaction
import com.paway.mobileapplication.presentation.reports.ReportViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


@Composable
fun ReportScreen(reportViewModel: ReportViewModel) {
    val errorState by reportViewModel.errorFlow.collectAsState()
    val transactions by reportViewModel.transactionFlow.collectAsState()

    var amount by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Monto de la transacción") }
        )


        TextField(
            value = details,
            onValueChange = { details = it },
            label = { Text("Detalles de la transacción") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // crear una nueva transacción
        Button(onClick = {
            val newTransaction = TransactionDto(
                id = UUID.randomUUID().toString(),
                amount = amount.toDoubleOrNull() ?: 0.0,
                date = formatDate(Date()),
                details = details,
                invoiceId = "670dcd2fb881c26baf26b9a0",
                userId = "23", // Cambiar por el ID del usuario real
                type = "Tipo blablabla",
                income = true
            )
            reportViewModel.createTransaction(newTransaction)
            reportViewModel.getTransactionsByUserId("23")
        }) {
            Text("Crear Transacción")
        }

        if (errorState != null) {
            Text("Error: $errorState", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text("Lista de Transacciones:", style = MaterialTheme.typography.titleMedium)


        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(transactions) { transaction ->
                TransactionCard(transaction)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Monto: ${transaction.amount}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Detalles: ${transaction.details}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Fecha: ${transaction.date}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Tipo: ${transaction.type}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// funcion auxiliar para formatear la fecha
fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    return dateFormat.format(date)
}