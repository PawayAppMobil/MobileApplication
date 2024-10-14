package com.paway.mobileapplication.invoces.data.repository

import com.paway.mobileapplication.common.Resource
import com.paway.mobileapplication.invoces.data.remote.WebService
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.toInvoice
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.toInvoiceRequestDto
import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Base64
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.toTransaction
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.toTransactionRequestDto
import com.paway.mobileapplication.invoces.domain.model.transaction.Transaction

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

    suspend fun createInvoice(invoice: Invoice, document: ByteArray?): Resource<Invoice> = withContext(Dispatchers.IO) {
        try {
            val invoiceRequestDto = invoice.toInvoiceRequestDto()
            val documentBase64 = document?.let { Base64.encodeToString(it, Base64.DEFAULT) }
            
            val response = webService.createInvoice(invoiceRequestDto, documentBase64)
            if (response.isSuccessful) {
                response.body()?.let { invoiceDto ->
                    return@withContext Resource.Success(data = invoiceDto.toInvoice())
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error("Error: ${response.code()} - ${response.errorBody()?.string()}")
        } catch (e: Exception) {
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
}
