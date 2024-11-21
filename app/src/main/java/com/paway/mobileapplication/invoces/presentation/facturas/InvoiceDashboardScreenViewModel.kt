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
    val totalOverdueInvoices: String = "0",
    val totalInvoices: String = "0",
    val daysToCheck: Int = 7,
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

    fun updateDaysToCheck(days: Int) {
        _state.value = _state.value.copy(daysToCheck = days)
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            userId?.let { id ->
                when (val result = repository.getInvoicesByUserId(id)) {
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

    private fun updateDashboardState(invoices: List<Invoice>) {
        val today = Calendar.getInstance()
        val futureDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, _state.value.daysToCheck)
        }

        val pendingInvoices = invoices.filter { invoice ->
            val dueDate = Calendar.getInstance().apply { time = invoice.dueDate }
            dueDate.after(today) && dueDate.before(futureDate)
        }

        val overdueInvoices = invoices.filter { invoice ->
            val dueDate = Calendar.getInstance().apply { time = invoice.dueDate }
            dueDate.before(today)
        }

        _state.value = _state.value.copy(
            totalPendingInvoices = "${pendingInvoices.sumOf { it.amount }}",
            totalOverdueInvoices = "S/. ${overdueInvoices.sumOf { it.amount }}",
            totalInvoices = "S/. ${invoices.sumOf { it.amount }}",
            isLoading = false
        )
    }
}
