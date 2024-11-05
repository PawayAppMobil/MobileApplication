package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.domain.Product
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.toInvoiceDTO
import com.paway.mobileapplication.invoces.data.repository.WebServiceRepository
import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import com.paway.mobileapplication.invoces.domain.model.transaction.Transaction
import kotlinx.coroutines.launch
import java.util.*

data class ImportInvoiceState(
    val invoice: Invoice = Invoice(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val selectedDocument: ByteArray? = null,
    val debugInfo: String = "",
    val availableProducts: List<Product> = emptyList(),
    val selectedProducts: List<Product> = emptyList()
)

class ImportInvoiceViewModel(private val repository: WebServiceRepository) : ViewModel() {
    private val _state = mutableStateOf(ImportInvoiceState())
    val state: State<ImportInvoiceState> = _state

    private var userId: String? = null

    fun setUserId(id: String) {
        userId = id
        _state.value = _state.value.copy(
            invoice = _state.value.invoice.copy(userId = id)
        )
        loadUserProducts()
    }

    private fun loadUserProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            userId?.let { id ->
                when (val result = repository.getProductsByUserId(id)) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            availableProducts = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Failed to load products: ${result.message}"
                        )
                    }
                }
            }
        }
    }

    fun toggleProductSelection(product: Product) {
        val currentSelected = _state.value.selectedProducts.toMutableList()
        if (currentSelected.contains(product)) {
            currentSelected.remove(product)
        } else {
            currentSelected.add(product)
        }
        _state.value = _state.value.copy(
            selectedProducts = currentSelected,
            invoice = _state.value.invoice.copy(
                items = currentSelected
            )
        )
    }

    fun createInvoiceAndTransaction() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, success = false)

            if (_state.value.selectedProducts.isEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Debe seleccionar al menos un producto"
                )
                return@launch
            }

            if (userId.isNullOrEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "User ID is not set"
                )
                return@launch
            }

            try {
                val invoice = _state.value.invoice.copy(
                    date = Date(),
                    amount = _state.value.selectedProducts.size.toDouble(),
                    items = _state.value.selectedProducts,
                    userId = userId ?: "",
                    status = _state.value.invoice.status.ifEmpty { "PENDING" }
                )

                when (val invoiceResult = repository.createInvoice(invoice)) {
                    is Resource.Success -> {
                        invoiceResult.data?.let { createdInvoice ->
                            _state.value.selectedDocument?.let { document ->
                                val documentResult = repository.uploadInvoiceDocument(
                                    createdInvoice.id,
                                    document
                                )
                                if (documentResult is Resource.Error) {
                                    _state.value = _state.value.copy(
                                        error = "Invoice created but failed to upload document: ${documentResult.message}"
                                    )
                                    return@launch
                                }
                            }

                            _state.value = _state.value.copy(
                                isLoading = false,
                                success = true,
                                error = null
                            )
                        } ?: run {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = "Created invoice data is null"
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Failed to create invoice: ${invoiceResult.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "An error occurred: ${e.message}"
                )
            }
        }
    }

    fun updateInvoiceDetails(
        date: Date? = null,
        dueDate: Date? = null,
        status: String? = null,
        document: ByteArray? = null,
        items: List<Product>? = null
    ) {
        val updatedInvoice = _state.value.invoice.copy(
            date = date ?: _state.value.invoice.date,
            status = status ?: _state.value.invoice.status,
            items = items ?: _state.value.invoice.items,
            userId = _state.value.invoice.userId,
            dueDate = dueDate ?: _state.value.invoice.dueDate,
            document = document ?: _state.value.invoice.document
        )
        _state.value = _state.value.copy(
            invoice = updatedInvoice,
            selectedDocument = document ?: _state.value.selectedDocument
        )
    }

    fun updateSelectedDocument(document: ByteArray) {
        updateInvoiceDetails(document = document)
    }
}
