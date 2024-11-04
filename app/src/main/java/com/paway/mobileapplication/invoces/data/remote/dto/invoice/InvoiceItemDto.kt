package com.paway.mobileapplication.invoces.data.remote.dto.invoice

data class InvoiceItemDto(
    val id: String,
    val description: String,
    val price: Double,
    val productName: String,
    val stock: Int,
    val providerId: String,
    val image: String? // Representa la imagen en base64
)