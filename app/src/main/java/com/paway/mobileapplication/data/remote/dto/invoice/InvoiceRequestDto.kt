package com.paway.mobileapplication.data.remote.dto.invoice

import com.paway.mobileapplication.domain.model.invoice.Invoice
import com.paway.mobileapplication.domain.model.invoice.InvoiceItem
import java.util.Date

data class InvoiceRequestDto(
    val date: Date,
    val status: String,
    val items: List<InvoiceItemDto>,
    val transactionId: String,
    val userId: String,
    val dueDate: Date
)

fun Invoice.toInvoiceRequestDto() = InvoiceRequestDto(
    date = date,
    status = status,
    items = items.map { it.toInvoiceItemDto() },
    transactionId = transactionId,
    userId = userId,
    dueDate = dueDate
)

fun InvoiceItem.toInvoiceItemDto() = InvoiceItemDto(
    id = id,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    productId = productId
)