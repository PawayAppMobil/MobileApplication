package com.paway.mobileapplication.invoces.data.remote.dto.invoice

import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import java.text.SimpleDateFormat
import java.util.*

data class InvoiceRequestDto(
    val date: String,
    val amount: Int,
    val status: String,
    val productIds: List<String>,
    val userId: String,
    val dueDate: String
)

fun Invoice.toInvoiceRequestDto(): InvoiceRequestDto {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return InvoiceRequestDto(
        date = dateFormat.format(date),
        amount = items.size,
        status = status,
        productIds = items.map { it.id },
        userId = userId,
        dueDate = dateFormat.format(dueDate)
    )
}
