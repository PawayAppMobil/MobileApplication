package com.paway.mobileapplication.invoces.presentation.facturas

import android.util.Base64
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
import java.io.File
import java.util.*

data class ImportInvoiceState(
    val invoice: Invoice = Invoice(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val selectedDocument: ByteArray? = null
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

    fun createInvoiceAndTransaction(documentFile: File?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, success = false)

            val invoiceResult = repository.createInvoice(_state.value.invoice, documentFile)
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
                                    error = null
                                )
                            }
                            is Resource.Error -> {
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    error = "Transaction creation failed: ${transactionResult.message}"
                                )
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Invoice creation failed: ${invoiceResult.message}"
                    )
                }
            }
        }
    }
}
