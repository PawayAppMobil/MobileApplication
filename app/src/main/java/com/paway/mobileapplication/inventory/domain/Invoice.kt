package com.paway.mobileapplication.inventory.domain

import com.paway.mobileapplication.reports.domain.model.invoice.InvoiceItem

data class Invoice(
    val id: String,
    val amount: Int,
    val status: String,
    val items: List<InvoiceItem>,
    val userId: String,
    val document: String
)