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
        val response = webService.getAllProducts()
        if (response.isSuccessful) {
            response.body()?.let { productDtos ->
                return@withContext Resource.Success(data = productDtos.map { it.toProduct() })
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updateInvoice(id: String, invoice: Invoice): Resource<Invoice> = withContext(Dispatchers.IO) {
        val response = webService.updateInvoice(id, invoice.toInvoiceRequestDto())
        if (response.isSuccessful) {
            response.body()?.let { invoiceDto ->
                return@withContext Resource.Success(data = invoiceDto.toInvoice())
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteInvoice(id: String): Resource<Unit> = withContext(Dispatchers.IO) {
        val response = webService.deleteInvoice(id)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun markInvoiceAsPaid(id: String): Resource<Invoice> = withContext(Dispatchers.IO) {
        val response = webService.markInvoiceAsPaid(id)
        if (response.isSuccessful) {
            response.body()?.let { invoiceDto ->
                return@withContext Resource.Success(data = invoiceDto.toInvoice())
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getAllInvoices(): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        val response = webService.getAllInvoices()
        if (response.isSuccessful) {
            response.body()?.let { invoiceDtos ->
                return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun createInvoice(invoice: Invoice): Resource<Invoice> = withContext(Dispatchers.IO) {
        val response = webService.createInvoice(invoice.toInvoiceRequestDto())
        if (response.isSuccessful) {
            response.body()?.let { invoiceResponseDto ->
                return@withContext Resource.Success(data = invoiceResponseDto.toInvoice())
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error("Error: ${response.code()} - ${response.errorBody()?.string()}")
    }

    suspend fun getInvoicesByStatus(status: String): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        val response = webService.getInvoicesByStatus(status)
        if (response.isSuccessful) {
            response.body()?.let { invoiceDtos ->
                return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getInvoicesByDueDateRange(startDate: String, endDate: String): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        val response = webService.getInvoicesByDueDateRange(startDate, endDate)
        if (response.isSuccessful) {
            response.body()?.let { invoiceDtos ->
                return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getInvoicesByCustomer(customerId: String): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        val response = webService.getInvoicesByCustomer(customerId)
        if (response.isSuccessful) {
            response.body()?.let { invoiceDtos ->
                return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun createTransaction(transaction: Transaction): Resource<Transaction> = withContext(Dispatchers.IO) {
        val response = webService.createTransaction(transaction.toTransactionRequestDto())
        if (response.isSuccessful) {
            response.body()?.let { transactionDto ->
                return@withContext Resource.Success(data = transactionDto.toTransaction())
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getProductsByUserId(userId: String): Resource<List<Product>> = withContext(Dispatchers.IO) {
        val response = webService.getProductsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { productDtos ->
                return@withContext Resource.Success(data = productDtos.map { it.toProduct() })
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getInvoicesByUserId(userId: String): Resource<List<Invoice>> = withContext(Dispatchers.IO) {
        val response = webService.getInvoicesByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { invoiceDtos ->
                return@withContext Resource.Success(data = invoiceDtos.map { it.toInvoice() })
            }
            return@withContext Resource.Error("Empty response body")
        }
        return@withContext Resource.Error(response.message())
    }
}
