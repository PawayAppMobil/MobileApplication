package com.paway.mobileapplication.invoces.domain.model.invoice

data class InvoiceItem(
    val id: String,
    val description: String,
    val price: Double,
    val productName: String,
    val stock: Int,
    val providerId: String,
    val image: String?
)