package com.paway.mobileapplication.reports.presentation.reports

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.paway.mobileapplication.reports.data.remote.dto.report.DateRange
import com.paway.mobileapplication.reports.domain.model.report.Report
import com.paway.mobileapplication.reports.domain.model.report.Transaction
import kotlin.math.absoluteValue

@Composable
fun ReportDetailScreen(report: Report, onBack: () -> Unit) {
    // Calcular balance
    val totalBalance = report.totalIncome - report.totalExpenses

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botón de regreso en la parte superior izquierda de la pantalla
        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tarjeta con detalles del reporte
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Detalles del Reporte",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Mostrar balance total
                Text(
                    text = "Balance Total: ${if (totalBalance >= 0) "+S/" else "-S/"}${totalBalance.absoluteValue}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (totalBalance >= 0) Color.Green else Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta con el gráfico de ingresos y gastos
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ingresos y Gastos",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Mostrar gráfico
                AndroidView(
                    factory = { context ->
                        BarChart(context).apply {
                            val barEntries = listOf(
                                BarEntry(0f, report.totalIncome.toFloat()), // Ingresos
                                BarEntry(1f, report.totalExpenses.toFloat()) // Gastos
                            )
                            val barDataSet = BarDataSet(barEntries, "").apply {
                                colors = listOf(android.graphics.Color.GREEN, android.graphics.Color.RED)
                                valueTextColor = android.graphics.Color.WHITE // Cambia el color del texto de los valores
                                valueTextSize = 12f // Tamaño del texto de los valores
                            }
                            this.data = BarData(barDataSet).apply { barWidth = 0.4f }
                            description.isEnabled = false
                            xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Ingresos", "Gastos"))
                            xAxis.textColor = android.graphics.Color.WHITE // Cambia el color del texto del eje X
                            xAxis.granularity = 1f
                            axisLeft.textColor = android.graphics.Color.WHITE // Cambia el color del texto del eje Y
                            axisLeft.setDrawGridLines(false)
                            axisRight.isEnabled = false
                            animateY(1000)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Reduce la altura del gráfico
                        .padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Transacciones",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Lista de transacciones en tarjetas
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            items(report.transactions) { transaction ->
                TransactionCard(transaction = transaction)
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = transaction.date,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = transaction.details,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (transaction.income) "+S/${transaction.amount}" else "-S/${transaction.amount}",
                style = MaterialTheme.typography.bodySmall,
                color = if (transaction.income) Color.Green else Color.Red
            )
        }
    }
}
