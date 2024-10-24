package com.paway.mobileapplication.invoces.data.remote

import com.paway.mobileapplication.invoces.data.remote.dto.invoice.InvoiceRequestDto
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.InvoiceResponseDto
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.TransactionRequestDto
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.TransactionResponseDto
import retrofit2.Response
import retrofit2.http.*

interface WebService {
    @GET("/api/invoices/{id}/")
    suspend fun getInvoice(@Path("id") id: String): Response<InvoiceResponseDto>

    @PUT("/api/invoices/{id}/")
    suspend fun updateInvoice(@Path("id") id: String, @Body invoice: InvoiceRequestDto): Response<InvoiceResponseDto>

    @DELETE("/api/invoices/{id}/")
    suspend fun deleteInvoice(@Path("id") id: String): Response<Unit>

    @PUT("/api/invoices/{id}/mark-as-paid/")
    suspend fun markInvoiceAsPaid(@Path("id") id: String): Response<InvoiceResponseDto>

    @GET("/api/invoices/")
    suspend fun getAllInvoices(): Response<List<InvoiceResponseDto>>

    @POST("/api/invoices/")
    suspend fun createInvoice(@Body invoice: InvoiceRequestDto): Response<InvoiceResponseDto>

    @GET("/api/invoices/status/{status}/")
    suspend fun getInvoicesByStatus(@Path("status") status: String): Response<List<InvoiceResponseDto>>

    @GET("/api/invoices/due-date-range/")
    suspend fun getInvoicesByDueDateRange(@Query("startDate") startDate: String, @Query("endDate") endDate: String): Response<List<InvoiceResponseDto>>

    @GET("/api/invoices/customer/{customerId}/")
    suspend fun getInvoicesByCustomer(@Path("customerId") customerId: String): Response<List<InvoiceResponseDto>>

    @POST("/api/transactions/")
    suspend fun createTransaction(@Body transaction: TransactionRequestDto): Response<TransactionResponseDto>
}
