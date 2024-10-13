package com.paway.mobileapplication.invoces.data.remote.dto.invoice

import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
import java.util.Date

data class InvoiceResponseDto(
    val id: String,
    val date: Date,
    val amount: Double,
    val status: String,
    val items: List<InvoiceItemDto>,
    val transactionId: String,
    val userId: String,
    val dueDate: Date
)
fun InvoiceResponseDto.toInvoice() = Invoice(
    id = id,
    date = date,
    amount = amount,
    status = status,
    items = items.map { it.toInvoiceItem() },
    transactionId = transactionId,
    userId = userId,
    dueDate = dueDate
)

fun InvoiceItemDto.toInvoiceItem() = InvoiceItem(
    id = id,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    productId = productId
)
