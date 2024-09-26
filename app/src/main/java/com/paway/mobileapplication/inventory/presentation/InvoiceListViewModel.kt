package com.paway.mobileapplication.inventory.presentation
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.common.UIState
import com.paway.mobileapplication.inventory.data.repository.InvoiceRepository
import com.paway.mobileapplication.inventory.domain.Invoice
import kotlinx.coroutines.launch

class InvoiceListViewModel(private val repository: InvoiceRepository) : ViewModel() {
    private val _state = mutableStateOf(UIState<List<Invoice>>())
    val state: State<UIState<List<Invoice>>> get() = _state

    private val _status = mutableStateOf("")
    val status: State<String> get() = _status

    fun onStatusChanged(status: String="pagar") {
        _status.value = status
        getInvoices()
    }

    private fun getInvoices() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getInvoicesByStatus(_status.value)

            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred.")
            }
        }
    }
}

