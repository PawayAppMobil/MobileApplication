package com.paway.mobileapplication.inventory.data.remote

import com.google.gson.annotations.SerializedName
import com.paway.mobileapplication.inventory.domain.Invoice

data class InvoiceDto(
    @SerializedName("id") val id: String,
    @SerializedName("date") val date: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("status") val status: String,
    @SerializedName("items") val items: List<InvoiceItemDto>,
    @SerializedName("transactionId") val transactionId: String,
    @SerializedName("customerId") val customerId: String,
    @SerializedName("dueDate") val dueDate: String
)
fun InvoiceDto.toInvoice()= Invoice(id, amount, status)
