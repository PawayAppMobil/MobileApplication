package com.paway.mobileapplication.invoces.data.remote

import com.paway.mobileapplication.inventory.data.remote.ProductDto
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.CreateInvoiceResponse
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.InvoiceRequestDto
import com.paway.mobileapplication.invoces.data.remote.dto.invoice.InvoiceResponseDto
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.TransactionRequestDto
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.TransactionResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface WebService {
    @GET("/api/invoices/{id}/")
    suspend fun getInvoice(@Path("id") id: String): Response<InvoiceResponseDto>

    @PUT("invoices/{id}")
    suspend fun updateInvoice(@Path("id") id: String, @Body invoice: InvoiceRequestDto): Response<InvoiceResponseDto>

    @DELETE("/api/invoices/{id}/")
    suspend fun deleteInvoice(@Path("id") id: String): Response<Unit>

    @PUT("/api/invoices/{id}/mark-as-paid/")
    suspend fun markInvoiceAsPaid(@Path("id") id: String): Response<InvoiceResponseDto>

    @GET("/api/invoices/")
    suspend fun getAllInvoices(): Response<List<InvoiceResponseDto>>

    @POST("invoices")
    suspend fun createInvoice(@Body invoice: InvoiceRequestDto): Response<CreateInvoiceResponse>

    @Multipart
    @POST("/api/invoices/{id}/document")
    suspend fun uploadInvoiceDocument(
        @Path("id") id: String,
        @Part document: MultipartBody.Part
    ): Response<Unit>

    @GET("/api/invoices/status/{status}/")
    suspend fun getInvoicesByStatus(@Path("status") status: String): Response<List<InvoiceResponseDto>>

    @GET("/api/invoices/due-date-range/")
    suspend fun getInvoicesByDueDateRange(@Query("startDate") startDate: String, @Query("endDate") endDate: String): Response<List<InvoiceResponseDto>>

    @GET("/api/invoices/customer/{customerId}/")
    suspend fun getInvoicesByCustomer(@Path("customerId") customerId: String): Response<List<InvoiceResponseDto>>

    @POST("/api/transactions/")
    suspend fun createTransaction(@Body transaction: TransactionRequestDto): Response<TransactionResponseDto>

    @GET("/api/invoices/user/{userId}")
    suspend fun getInvoicesByUserId(@Path("userId") userId: String): Response<List<InvoiceResponseDto>>

    @GET("/api/products/")
    suspend fun getAllProducts(): Response<List<ProductDto>>

    @GET("/api/products/user/{userId}")
    suspend fun getProductsByUserId(@Path("userId") userId: String): Response<List<ProductDto>>
}
