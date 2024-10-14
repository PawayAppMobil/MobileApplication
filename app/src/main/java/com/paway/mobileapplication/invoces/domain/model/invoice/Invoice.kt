package com.paway.mobileapplication.invoces.domain.model.invoice

import InvoiceDTO
import java.util.Date

data class Invoice(
    val id: String = "",
    val date: Date = Date(),
    val amount: Double = 0.0,
    val status: String = "",
    val items: List<InvoiceItem> = emptyList(),
    val transactionId: String = "",
    val userId: String = "",
    val dueDate: Date = Date(),
    val document: ByteArray? = null
) {
    fun toInvoiceDTO() = InvoiceDTO(
        date = date,
        amount = amount,
        status = status,
        items = items,
        transactionId = transactionId,
        userId = userId,
        dueDate = dueDate
    )
}
