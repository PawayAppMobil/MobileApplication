package com.paway.mobileapplication.invoces.domain.model.invoice

data class InvoiceItem(
    val id: String,
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val productId: String
)