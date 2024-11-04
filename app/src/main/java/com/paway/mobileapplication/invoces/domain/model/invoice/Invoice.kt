package com.paway.mobileapplication.invoces.domain.model.invoice

import InvoiceDTO
import com.paway.mobileapplication.inventory.domain.Product
import java.util.Date

data class Invoice(
    val id: String = "",
    val date: Date = Date(),
    val amount: Double = 0.0,
    val status: String = "",
    val items: List<Product> = listOf(),  // Lista de productos completos
    val userId: String = "",
    val dueDate: Date = Date(),
    val document: ByteArray? = null
) {
    fun toInvoiceDTO() = InvoiceDTO(
        date = date,
        amount = amount,
        status = status,
        productIds = items.map { it.id },  // Convertimos la lista de productos a lista de IDs
        userId = userId,
        dueDate = dueDate
    )
}