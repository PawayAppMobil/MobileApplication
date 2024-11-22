import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.paway.mobileapplication.common.Resource
import com.paway.mobileapplication.reports.domain.model.report.Report
import com.paway.mobileapplication.reports.presentation.reports.ReportDetailScreen
import com.paway.mobileapplication.reports.presentation.reports.ReportViewModel


@Composable
fun ReportScreen(viewModel: ReportViewModel, userId: String) {
    viewModel.initialize(userId)
    val reportState by viewModel.reportFlow.collectAsState()
    val localReports by viewModel.localReportFlow.collectAsState()

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var reportType by remember { mutableStateOf("") }
    var selectedReport by remember { mutableStateOf<Report?>(null) }

    if (selectedReport != null) {
        ReportDetailScreen(report = selectedReport!!) {
            selectedReport = null
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Generar Nuevo Reporte",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = reportType,
                        onValueChange = { reportType = it },
                        label = { Text("Tipo de Reporte") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Fecha Inicio (AAAA-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("Fecha Fin (AAAA-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            viewModel.createReport(
                                userId,
                                reportType,
                                startDate,
                                endDate
                            )
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Generar Reporte")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val combinedReports = when (reportState) {
                is Resource.Success -> {
                    val fetchedReports = (reportState as Resource.Success<List<Report>>).data ?: emptyList()
                    // Combina los reportes obtenidos y los locales sin duplicados
                    val nonDuplicateReports = fetchedReports + localReports.filterNot { fetchedReports.any { remoteReport -> remoteReport.id == it.id } }
                    nonDuplicateReports
                }
                else -> localReports
            }

            if (combinedReports.isEmpty()) {
                Text(
                    text = "No hay reportes disponibles",
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Text(
                    text = "Lista de Reportes",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn {
                    items(combinedReports) { report ->
                        ReportItem(
                            report = report,
                            onDelete = { reportId -> viewModel.deleteReport(userId, reportId) },
                            onSelect = { selectedReport = it }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ReportItem(report: Report, onDelete: (String) -> Unit, onSelect: (Report) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp),
        onClick = { onSelect(report) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Fecha de generacion: ${report.generatedAt}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tipo: ${report.reportType}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Total Ingresos: ${report.totalIncome}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Total Gastos: ${report.totalExpenses}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { onDelete(report.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Report",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}