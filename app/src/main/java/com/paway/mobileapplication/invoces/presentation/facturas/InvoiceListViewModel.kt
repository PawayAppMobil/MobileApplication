package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.invoces.data.repository.WebServiceRepository
import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class InvoiceListState(
    val invoices: List<Invoice> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class InvoiceListViewModel(
    private val repository: WebServiceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(InvoiceListState())
    val state: StateFlow<InvoiceListState> = _state

    private var userId: String? = null

    fun setUserId(id: String) {
        userId = id
        loadInvoices()
    }

    fun loadInvoices() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            userId?.let { id ->
                when (val result = repository.getInvoicesByUserId(id)) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            invoices = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
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

    fun reviewDetails() {
        // Implement review details logic
    }

    fun confirmInvoices() {
        // Implement confirm invoices logic
    }

    fun discardInvoices() {
        // Implement discard invoices logic
    }
} 