package com.paway.mobileapplication.invoces.domain.model.invoice

import java.util.Date

data class Invoice(
    val id: String,
    val date: Date,
    val amount: Double,
    val status: String,
    val items: List<InvoiceItem>,
    val transactionId: String,
    val userId: String,
    val dueDate: Date
)