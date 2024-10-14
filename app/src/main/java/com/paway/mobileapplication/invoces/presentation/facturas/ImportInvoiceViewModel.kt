package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.common.Resource
import com.paway.mobileapplication.invoces.data.repository.WebServiceRepository
import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
import com.paway.mobileapplication.invoces.domain.model.transaction.Transaction
import kotlinx.coroutines.launch
import java.util.*

data class ImportInvoiceState(
    val invoice: Invoice = Invoice(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val selectedDocument: ByteArray? = null,
    val debugInfo: String = ""
)

class ImportInvoiceViewModel(private val repository: WebServiceRepository) : ViewModel() {
    private val _state = mutableStateOf(ImportInvoiceState())
    val state: State<ImportInvoiceState> = _state

    fun setUserId(userId: String) {
        _state.value = _state.value.copy(invoice = _state.value.invoice.copy(userId = userId))
    }

    fun addInvoiceItem(item: InvoiceItem) {
        val currentItems = _state.value.invoice.items.toMutableList()
        currentItems.add(item)
        updateInvoice(items = currentItems)
    }

    fun removeInvoiceItem(item: InvoiceItem) {
        val currentItems = _state.value.invoice.items.toMutableList()
        currentItems.remove(item)
        updateInvoice(items = currentItems)
    }

    fun updateInvoiceDetails(
        date: Date? = null,
        dueDate: Date? = null,
        status: String? = null,
        document: ByteArray? = null
    ) {
        updateInvoice(
            date = date,
            dueDate = dueDate,
            status = status,
            document = document
        )
        _state.value = _state.value.copy(selectedDocument = document)
    }

    private fun updateInvoice(
        date: Date? = null,
        amount: Double? = null,
        status: String? = null,
        items: List<InvoiceItem>? = null,
        transactionId: String? = null,
        userId: String? = null,
        dueDate: Date? = null,
        document: ByteArray? = null
    ) {
        _state.value = _state.value.copy(
            invoice = _state.value.invoice.copy(
                date = date ?: _state.value.invoice.date,
                amount = amount ?: _state.value.invoice.amount,
                status = status ?: _state.value.invoice.status,
                items = items ?: _state.value.invoice.items,
                transactionId = transactionId ?: _state.value.invoice.transactionId,
                userId = userId ?: _state.value.invoice.userId,
                dueDate = dueDate ?: _state.value.invoice.dueDate,
                document = document ?: _state.value.invoice.document
            )
        )
    }

    fun updateSelectedDocument(document: ByteArray) {
        _state.value = _state.value.copy(selectedDocument = document)
    }

    fun createInvoiceAndTransaction() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, success = false)

            if (_state.value.invoice.items.isEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "La factura debe tener al menos un item"
                )
                return@launch
            }

            val totalAmount = _state.value.invoice.items.sumOf { it.quantity * it.unitPrice }
            val updatedInvoice = _state.value.invoice.copy(
                amount = totalAmount,
                date = Date(),
                dueDate = Date(),  // Ajusta esto según tus necesidades
                status = "PENDING"  // O el estado que corresponda
            )

            // Actualizar el debugInfo con los datos que se van a enviar
            _state.value = _state.value.copy(
                debugInfo = "Enviando: ${updatedInvoice.toString()}\nDocumento: ${_state.value.selectedDocument != null}"
            )

            val invoiceResult = repository.createInvoice(updatedInvoice, _state.value.selectedDocument)
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
                                    debugInfo = _state.value.debugInfo + "\n\nRespuesta: ${createdInvoice.toString()}"
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
