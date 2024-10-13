package com.paway.mobileapplication.invoces.data.remote.dto.invoice

data class InvoiceItemDto(
    val id: String,
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val productId: String
)