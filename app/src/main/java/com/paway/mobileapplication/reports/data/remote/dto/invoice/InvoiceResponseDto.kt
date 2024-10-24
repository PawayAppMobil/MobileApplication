<<<<<<<< HEAD:app/src/main/java/com/paway/mobileapplication/invoces/data/remote/dto/invoice/InvoiceResponseDto.kt
package com.paway.mobileapplication.invoces.data.remote.dto.invoice

import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
========
package com.paway.mobileapplication.reports.data.remote.dto.invoice

import com.paway.mobileapplication.reports.domain.model.invoice.Invoice
import com.paway.mobileapplication.reports.domain.model.invoice.InvoiceItem
>>>>>>>> origin/feature-reportes:app/src/main/java/com/paway/mobileapplication/reports/data/remote/dto/invoice/InvoiceResponseDto.kt
import java.util.Date
import android.util.Base64

data class InvoiceResponseDto(
    val id: String,
    val date: Date,
    val amount: Double,
    val status: String,
    val items: List<InvoiceItemDto>,
    val transactionId: String,
    val userId: String,
    val dueDate: Date,
    val document: String? // Cambiado a String? para representar el documento en base64
)

fun InvoiceResponseDto.toInvoice() = Invoice(
    id = id,
    date = date,
    amount = amount,
    status = status,
    items = items.map { it.toInvoiceItem() },
    transactionId = transactionId,
    userId = userId,
    dueDate = dueDate,
    document = document?.let { Base64.decode(it, Base64.DEFAULT) }
)

fun InvoiceItemDto.toInvoiceItem() = InvoiceItem(
    id = id,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    productId = productId
)
