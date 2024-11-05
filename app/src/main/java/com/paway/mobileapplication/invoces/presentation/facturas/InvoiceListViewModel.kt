package com.paway.mobileapplication.invoces.presentation.facturas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.toInvoiceRequestDto
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

    fun setUserId(id: String?) {
        if (id.isNullOrEmpty()) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = "User ID cannot be null or empty"
            )
            return
        }
        
        userId = id
        loadInvoices()
    }

    fun loadInvoices() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            val currentUserId = userId
            if (currentUserId.isNullOrEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "User ID is not set. Please ensure you're logged in."
                )
                return@launch
            }

            try {
                println("🔍 Attempting to load invoices for user: $currentUserId")
                when (val result = repository.getInvoicesByUserId(currentUserId)) {
                    is Resource.Success -> {
                        println("✅ Successfully loaded invoices for user: $currentUserId")
                        _state.value = _state.value.copy(
                            invoices = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        println("❌ Error loading invoices for user: $currentUserId")
                        println("Error details: ${result.message}")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Error loading invoices: ${result.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                println("💥 Exception while loading invoices: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "An unexpected error occurred: ${e.message}"
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

    fun updateInvoice(invoice: Invoice) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            if (invoice.id.isEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error: ID de factura vacío"
                )
                return@launch
            }

            try {
                val requestJson = invoice.toInvoiceRequestDto()
                println("🔍 Validación de datos:")
                println("ID de factura: ${invoice.id}")
                println("URL esperada: /invoices/${invoice.id}")
                println("Request JSON: $requestJson")
                
                when (val result = repository.updateInvoice(invoice.id, invoice)) {
                    is Resource.Success -> {
                        println("✅ Factura actualizada exitosamente")
                        loadInvoices()
                    }
                    is Resource.Error -> {
                        val errorDetails = when {
                            result.message?.contains("404") == true -> {
                                """
                                Error 404: Factura no encontrada
                                ID: ${invoice.id}
                                Detalles adicionales: ${result.message}
                                Esto es inesperado ya que la factura existe en la lista.
                                Por favor, contacte al soporte técnico.
                                """
                            }
                            result.message?.contains("400") == true -> {
                                """
                                Error 400: Datos inválidos
                                - Asegúrese de que todos los campos requeridos estén presentes
                                - Verifique el formato de las fechas
                                - Verifique el formato de los IDs de productos
                                Detalles: ${result.message}
                                """
                            }
                            else -> "Error desconocido: ${result.message}"
                        }.trimIndent()

                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = errorDetails
                        )
                    }
                }
            } catch (e: Exception) {
                val errorMessage = """
                    Error inesperado al actualizar la factura
                    Tipo: ${e.javaClass.simpleName}
                    Mensaje: ${e.message}
                    Por favor, intente nuevamente o contacte al soporte técnico.
                """.trimIndent()

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
            }
        }
    }

    fun deleteInvoice(invoiceId: String) {
        viewModelScope.launch {
            when (val result = repository.deleteInvoice(invoiceId)) {
                is Resource.Success -> {
                    loadInvoices()
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = "Error deleting invoice: ${result.message}"
                    )
                }
            }
        }
    }
} 