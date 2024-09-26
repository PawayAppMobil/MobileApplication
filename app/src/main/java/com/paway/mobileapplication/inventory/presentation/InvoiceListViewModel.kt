package com.paway.mobileapplication.inventory.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.common.UIState
import com.paway.mobileapplication.inventory.data.repository.InvoiceRepository
import com.paway.mobileapplication.inventory.domain.Invoice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InvoiceListViewModel(private val repository: InvoiceRepository) : ViewModel() {
    private val _statePagar = MutableStateFlow(UIState<List<Invoice>>())
    val statePagar: StateFlow<UIState<List<Invoice>>> get() = _statePagar

    private val _statePagado = MutableStateFlow(UIState<List<Invoice>>())
    val statePagado: StateFlow<UIState<List<Invoice>>> get() = _statePagado

    private val _totalQuantityPagar = MutableStateFlow(0)
    val totalQuantityPagar: StateFlow<Int> get() = _totalQuantityPagar

    private val _totalAmountPagar = MutableStateFlow(0)
    val totalAmountPagar: StateFlow<Int> get() = _totalAmountPagar

    private val _totalQuantityPagado = MutableStateFlow(0)
    val totalQuantityPagado: StateFlow<Int> get() = _totalQuantityPagado

    private val _totalAmountPagado = MutableStateFlow(0)
    val totalAmountPagado: StateFlow<Int> get() = _totalAmountPagado

    fun loadInvoices() {
        getInvoices("pagar")
        getInvoices("pagado")
    }

    private fun getInvoices(status: String) {
        val stateFlow = if (status == "pagar") _statePagar else _statePagado
        val totalQuantityFlow = if (status == "pagar") _totalQuantityPagar else _totalQuantityPagado
        val totalAmountFlow = if (status == "pagar") _totalAmountPagar else _totalAmountPagado

        stateFlow.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getInvoicesByStatus(status)

            if (result is Resource.Success) {
                val invoices = result.data ?: emptyList()
                stateFlow.value = UIState(data = invoices)
                totalQuantityFlow.value = invoices.sumOf { invoice -> invoice.items.sumOf { it.quantity } }
                totalAmountFlow.value = invoices.sumOf { it.amount }
            } else {
                stateFlow.value = UIState(error = result.message ?: "An error occurred.")
            }
        }
    }
}
