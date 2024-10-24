<<<<<<<< HEAD:app/src/main/java/com/paway/mobileapplication/invoces/data/remote/dto/invoice/InvoiceRequestDto.kt
package com.paway.mobileapplication.invoces.data.remote.dto.invoice

import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
========
package com.paway.mobileapplication.reports.data.remote.dto.invoice

import com.paway.mobileapplication.reports.domain.model.invoice.Invoice
import com.paway.mobileapplication.reports.domain.model.invoice.InvoiceItem
>>>>>>>> origin/feature-reportes:app/src/main/java/com/paway/mobileapplication/reports/data/remote/dto/invoice/InvoiceRequestDto.kt
import java.util.Date
import android.util.Base64

data class InvoiceRequestDto(
    val date: Date,
    val amount: Double,
    val status: String,
    val items: List<InvoiceItemDto>,
    val transactionId: String,
    val userId: String,
    val dueDate: Date,
    val document: String? // Cambiado a String? para representar el documento en base64
)

fun Invoice.toInvoiceRequestDto() = InvoiceRequestDto(
    date = date,
    amount = amount,
    status = status,
    items = items.map { it.toInvoiceItemDto() },
    transactionId = transactionId,
    userId = userId,
    dueDate = dueDate,
    document = document?.let { Base64.encodeToString(it, Base64.DEFAULT) }
)

fun InvoiceItem.toInvoiceItemDto() = InvoiceItemDto(
    id = id,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    productId = productId
)
