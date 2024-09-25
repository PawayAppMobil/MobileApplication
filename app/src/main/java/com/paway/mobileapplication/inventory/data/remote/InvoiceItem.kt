package com.paway.mobileapplication.inventory.data.remote

data class InvoiceItem(
    val id: String,
    val description: String,
    val quantity: Int,
    val unitPrice: Int,
    val productId: String
)