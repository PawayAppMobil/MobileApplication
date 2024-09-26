package com.paway.mobileapplication.inventory.data.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.data.remote.InvoiceDto
import com.paway.mobileapplication.inventory.data.remote.InvoiceService
import com.paway.mobileapplication.inventory.data.remote.toInvoice

class InvoiceRepository(private val service: InvoiceService) {

    suspend fun getInvoicesByStatus(status: String) = withContext(Dispatchers.IO) {
        try {
            val response = service.getInvoicesByStatus(status)
            if (response.isSuccessful) {
                response.body()?.let { invoicesDto: List<InvoiceDto> ->
                    val invoices = invoicesDto.map { invoiceDto: InvoiceDto ->
                        invoiceDto.toInvoice()
                    }.toList()
                    return@withContext Resource.Success(data = invoices)
                }
            }
            return@withContext Resource.Error(message = response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An exception occurred.")
        }
    }
}
