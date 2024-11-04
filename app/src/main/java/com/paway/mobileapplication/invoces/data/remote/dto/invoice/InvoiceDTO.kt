package com.paway.mobileapplication.invoces.data.remote.dto.invoice

import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import java.util.*

data class InvoiceDTO(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val date: Date,
    val dueDate: Date,
    val status: String,
    val items: List<ProductDTO>,
    val document: ByteArray? = null,
    val amount: Double = 0.0
)

fun Invoice.toInvoiceDTO(): InvoiceDTO {
    return InvoiceDTO(
        id = this.id,
        userId = this.userId,
        date = this.date,
        dueDate = this.dueDate,
        status = this.status,
        items = this.items.map { it.toProductDTO() },
        document = this.document,
        amount = this.amount
    )
}
