package com.paway.mobileapplication.invoces.data.remote.dto.invoice

import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
import java.util.Date

data class InvoiceRequestDto(
    val date: Date,
    val amount: Double,
    val status: String,
    val items: List<InvoiceItemDto>,
    val transactionId: String,
    val userId: String,
    val dueDate: Date,
    val document: List<String>
)

fun Invoice.toInvoiceRequestDto() = InvoiceRequestDto(
    date = date,
    amount = amount,
    status = status,
    items = items.map { it.toInvoiceItemDto() },
    transactionId = transactionId,
    userId = userId,
    dueDate = dueDate,
    document = document
)

fun InvoiceItem.toInvoiceItemDto() = InvoiceItemDto(
    id = id,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    productId = productId
)
