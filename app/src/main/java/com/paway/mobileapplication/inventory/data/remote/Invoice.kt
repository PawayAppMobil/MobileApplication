package com.paway.mobileapplication.inventory.data.remote

data class Invoice(
    val id: String,
    val date: String,
    val amount: Int,
    val status: String,
    val items: List<InvoiceItem>,
    val transactionId: String,
    val customerId: String,
    val dueDate: String
)