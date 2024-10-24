package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.invoces.data.repository.WebServiceRepository
import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import kotlinx.coroutines.launch
import java.util.*

data class DashboardUIState(
    val totalPendingInvoices: String = "0",
    val totalScheduledPayments: String = "0",
    val totalAlerts: String = "0",
    val isLoading: Boolean = false,
    val error: String? = null
)

class InvoiceDashboardViewModel(private val repository: WebServiceRepository) : ViewModel() {

    private val _state = mutableStateOf(DashboardUIState())
    val state: State<DashboardUIState> = _state

    private var userId: String? = null

    fun setUserId(id: String) {
        userId = id
        loadDashboardData()
    }

    fun loadDashboardData() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            userId?.let { id ->
                when (val result = repository.getInvoicesByCustomer(id)) {
                    is Resource.Success -> {
                        val invoices = result.data ?: emptyList()
                        updateDashboardState(invoices)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }
            } ?: run {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "User ID not set"
                )
            }
        }
    }

    // REFERENCIA
    // Actualiza el estado del panel de control con el total de facturas pendientes, pagos programados y alertas.
    // Filtra las facturas para determinar las pendientes y los pagos programados, y luego calcula las alertas.
    private fun updateDashboardState(invoices: List<Invoice>) {
        val pendingInvoices = invoices.filter { it.status == "PENDING" }
        val scheduledPayments = invoices.filter { it.status == "SCHEDULED" }
        val alerts = calculateAlerts(invoices)

        _state.value = _state.value.copy(
            totalPendingInvoices = "S/. ${pendingInvoices.sumOf { it.amount }.toString()}",
            totalScheduledPayments = "S/. ${scheduledPayments.sumOf { it.amount }.toString()}",
            totalAlerts = alerts.toString(),
            isLoading = false
        )
    }
    // REFERENCIA
    // Calcula cuántas facturas pendientes tienen una fecha de vencimiento dentro de los próximos 7 días.
    // Retorna el número de facturas que cumplen con este criterio.
    private fun calculateAlerts(invoices: List<Invoice>): Int {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val sevenDaysLater = calendar.time

        return invoices.count { invoice ->
            invoice.status == "PENDING" && invoice.dueDate.before(sevenDaysLater)
        }
    }
}
