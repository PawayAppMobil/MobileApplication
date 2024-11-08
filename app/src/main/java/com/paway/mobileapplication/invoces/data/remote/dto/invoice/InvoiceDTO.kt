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

