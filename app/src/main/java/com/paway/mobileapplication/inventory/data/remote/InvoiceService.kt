package com.paway.mobileapplication.inventory.data.remote

import retrofit2.http.*


interface InvoiceService {

    @GET("invoices/{id}")
    suspend fun getInvoiceById(@Path("id") id: String): InvoiceDto

    @PUT("invoices/{id}")
    suspend fun updateInvoice(@Path("id") id: String, @Body invoiceDto: InvoiceDto): InvoiceDto

    @DELETE("invoices/{id}")
    suspend fun deleteInvoice(@Path("id") id: String)

    @PUT("invoices/{id}/mark-as-paid")
    suspend fun markInvoiceAsPaid(@Path("id") id: String): InvoiceDto

    @GET("invoices")
    suspend fun getAllInvoices(): List<InvoiceDto>

    @POST("invoices")
    suspend fun createInvoice(@Body invoiceDto: InvoiceDto): InvoiceDto

    @GET("invoices/status/{status}")
    suspend fun getInvoicesByStatus(@Path("status") status: String): List<InvoiceDto>

    @GET("invoices/due-date-range")
    suspend fun getInvoicesByDueDateRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<InvoiceDto>

    @GET("invoices/customer/{customerId}")
    suspend fun getInvoicesByCustomerId(@Path("customerId") customerId: String): List<InvoiceDto>
}
