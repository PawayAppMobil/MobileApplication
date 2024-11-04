package com.paway.mobileapplication.invoces.data.remote.dto.invoice

import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import java.util.Date
import android.util.Base64
import com.paway.mobileapplication.inventory.domain.Product


data class InvoiceResponseDto(
    val id: String,
    val date: Date,
    val amount: Double,
    val status: String,
    val items: List<Product>, // Ahora directamente usa Product
    val userId: String,
    val dueDate: Date,
    val document: String? // Representa el documento en base64
)

// Mapeo de InvoiceResponseDto a Invoice
fun InvoiceResponseDto.toInvoice() = Invoice(
    id = id,
    date = date,
    amount = amount,
    status = status,
    items = items, // Se asigna directamente ya que ahora son Product
    userId = userId,
    dueDate = dueDate,
    document = document?.let { Base64.decode(it, Base64.DEFAULT) }
)