package com.paway.mobileapplication.invoces.data.repository

import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.data.remote.toProduct
import com.paway.mobileapplication.inventory.domain.Product
import com.paway.mobileapplication.invoces.data.remote.WebService
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.toInvoice
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.toInvoiceRequestDto
import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.toTransaction
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.toTransactionRequestDto
import com.paway.mobileapplication.invoces.domain.model.transaction.Transaction
import okhttp3.MultipartBody
import java.util.*


class WebServiceRepository(private val webService: WebService) {

    suspend fun getInvoice(id: String): Resource<Invoice> = withContext(Dispatchers.IO) {
        try {
            val response = webService.getInvoice(id)
            if (response.isSuccessful) {
                response.body()?.let { invoiceDto ->
                    return@withContext Resource.Success(data = invoiceDto.toInvoice())
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }
    suspend fun getAllProducts(): Resource<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = webService.getAllProducts()
            if (response.isSuccessful) {
                response.body()?.let { productDtos ->
                    return@withContext Resource.Success(data = productDtos.map { it.toProduct() })
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }
    suspend fun updateInvoice(id: String, invoice: Invoice): Resource<Invoice> = withContext(Dispatchers.IO) {
        try {
            val response = webService.updateInvoice(id, invoice.toInvoiceRequestDto())
            if (response.isSuccessful) {
                response.body()?.let { invoiceDto ->
                    return@withContext Resource.Success(data = invoiceDto.toInvoice())
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun deleteInvoice(id: String): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = webService.deleteInvoice(id)
            if (response.isSuccessful) {
                return@withContext Resource.Success(Unit)
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun markInvoiceAsPaid(id: String): Resource<Invoice> = withContext(Dispatchers.IO) {
        try {
            val response = webService.markInvoiceAsPaid(id)
            if (response.isSuccessful) {
                response.body()?.let { invoiceDto ->
                    return@withContext Resource.Success(data = invoiceDto.toInvoice())
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getAllInvoices(): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        try {
            val response = webService.getAllInvoices()
            if (response.isSuccessful) {
                response.body()?.let { invoiceDtos ->
                    return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun createInvoice(invoice: Invoice): Resource<Invoice> = withContext(Dispatchers.IO) {
        try {
            println("📤 Enviando invoice: ${invoice.toInvoiceRequestDto()}")
            val invoiceRequestDto = invoice.toInvoiceRequestDto()
            val response = webService.createInvoice(invoiceRequestDto)
            
            println("📡 Código de respuesta: ${response.code()}")
            println("📦 Cuerpo de respuesta: ${response.body()}")
            
            if (response.isSuccessful) {
                response.body()?.let { invoiceResponseDto ->
                    return@withContext Resource.Success(data = invoiceResponseDto.toInvoice())
                }
                return@withContext Resource.Error("Empty response body")
            }
            
            val errorBody = response.errorBody()?.string()
            println("❌ Error en la respuesta: $errorBody")
            return@withContext Resource.Error("Error: ${response.code()} - $errorBody")
        } catch (e: Exception) {
            println("💥 Excepción al crear invoice: ${e.message}")
            println("Stack trace: ${e.stackTraceToString()}")
            return@withContext Resource.Error("An error occurred: ${e.message}")
        }
    }

    suspend fun getInvoicesByStatus(status: String): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        try {
            val response = webService.getInvoicesByStatus(status)
            if (response.isSuccessful) {
                response.body()?.let { invoiceDtos ->
                    return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getInvoicesByDueDateRange(startDate: String, endDate: String): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        try {
            val response = webService.getInvoicesByDueDateRange(startDate, endDate)
            if (response.isSuccessful) {
                response.body()?.let { invoiceDtos ->
                    return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getInvoicesByCustomer(customerId: String): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        try {
            val response = webService.getInvoicesByCustomer(customerId)
            if (response.isSuccessful) {
                response.body()?.let { invoiceDtos ->
                    return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun createTransaction(transaction: Transaction): Resource<Transaction> = withContext(Dispatchers.IO) {
        try {
            val response = webService.createTransaction(transaction.toTransactionRequestDto())
            if (response.isSuccessful) {
                response.body()?.let { transactionDto ->
                    return@withContext Resource.Success(data = transactionDto.toTransaction())
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getProductsByUserId(userId: String): Resource<List<Product>> = withContext(Dispatchers.IO) {
            try {
                val response = webService.getProductsByUserId(userId)
                if (response.isSuccessful) {
                    response.body()?.let { productDtos ->
                        return@withContext Resource.Success(data = productDtos.map { it.toProduct() })
                    }
                    return@withContext Resource.Error("Empty response body")
                }
                return@withContext Resource.Error(response.message())
            } catch (e: Exception) {
                return@withContext Resource.Error(e.message ?: "An error occurred")
            }
        }
    suspend fun getInvoicesByUserId(userId: String): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        try {
            println("⭐ Iniciando petición para userId: $userId")
            println("🔍 URL esperada: https://paway-app-68cea87ac8ef.herokuapp.com/api/invoices/user/$userId")
            val response = webService.getInvoicesByUserId(userId)
            println("📡 Código de respuesta: ${response.code()}")

            // Log detallado de la respuesta
            println("🔍 Headers de respuesta: ${response.headers()}")
            println("📦 Cuerpo de respuesta: ${response.body()}")
            
            if (response.isSuccessful) {
                val body = response.body()
                println("✅ Respuesta exitosa")
                println("📝 Número de facturas recibidas: ${body?.size ?: 0}")
                
                body?.let { invoiceDtos ->
                    return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
                }
                println("⚠️ Cuerpo de respuesta vacío")
                return@withContext Resource.Error("Empty response body")
            }
            
            // Log detallado del error
            val errorBody = response.errorBody()?.string()
            println("❌ Error en la respuesta")
            println("⚠️ Código de error: ${response.code()}")
            println("📄 Cuerpo del error: $errorBody")
            
            return@withContext Resource.Error(
                "Error ${response.code()}: ${response.message()}\nDetalles: $errorBody"
            )
        } catch (e: Exception) {
            println("💥 Excepción: ${e.message}")
            println("Stack trace: ${e.stackTraceToString()}")
            return@withContext Resource.Error(
                "Error de red: ${e.message}\nTipo: ${e.javaClass.simpleName}"
            )
        }
    }
}
