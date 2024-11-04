package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.domain.Product
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

    init {
        loadAvailableProducts()
    }

    private fun loadAvailableProducts() {
        viewModelScope.launch {
            when (val result = repository.getAllProducts()) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(availableProducts = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(error = "Failed to load products: ${result.message}")
                }
            }
        }
    }

    fun setUserId(userId: String) {
        _state.value = _state.value.copy(invoice = _state.value.invoice.copy(userId = userId))
    }

    fun toggleProductSelection(product: Product) {
        val currentSelected = _state.value.selectedProducts.toMutableList()
        if (currentSelected.contains(product)) {
            currentSelected.remove(product)
        } else {
            currentSelected.add(product)
        }
        _state.value = _state.value.copy(selectedProducts = currentSelected)

        updateInvoiceDetails(items = currentSelected)
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

            val updatedInvoice = _state.value.invoice.copy(
                date = Date(),
                items = _state.value.selectedProducts,
                dueDate = _state.value.invoice.dueDate ?: Date(),
                status = _state.value.invoice.status.ifEmpty { "PENDING" }
            )

            _state.value = _state.value.copy(
                debugInfo = "Enviando: ${updatedInvoice.toInvoiceDTO()}\nDocumento: ${_state.value.selectedDocument != null}"
            )

            val invoiceResult = repository.createInvoice(updatedInvoice)
            when (invoiceResult) {
                is Resource.Success -> {
                    val createdInvoice = invoiceResult.data
                    createdInvoice?.let { invoice ->
                        val transaction = Transaction(
                            id = UUID.randomUUID().toString(),
                            amount = invoice.amount,
                            date = invoice.date,
                            details = "Invoice ${invoice.id} created",
                            invoiceId = invoice.id,
                            userId = invoice.userId,
                            type = "Income",
                            income = true
                        )

                        val transactionResult = repository.createTransaction(transaction)
                        when (transactionResult) {
                            is Resource.Success -> {
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    success = true,
                                    error = null,
                                    debugInfo = _state.value.debugInfo + "\n\nRespuesta: $createdInvoice"
                                )
                            }
                            is Resource.Error -> {
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    error = "Transaction creation failed: ${transactionResult.message}",
                                    debugInfo = _state.value.debugInfo + "\n\nError en transacción: ${transactionResult.message}"
                                )
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Invoice creation failed: ${invoiceResult.message}",
                        debugInfo = _state.value.debugInfo + "\n\nError en factura: ${invoiceResult.message}"
                    )
                }
            }
        }
    }
}
