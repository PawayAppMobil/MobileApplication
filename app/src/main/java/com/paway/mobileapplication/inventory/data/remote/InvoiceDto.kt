package com.paway.mobileapplication.inventory.data.remote

import com.google.gson.annotations.SerializedName
import com.paway.mobileapplication.inventory.domain.Invoice

data class InvoiceDto(
    @SerializedName("id") val id: String,
    @SerializedName("date") val date: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("status") val status: String,
    @SerializedName("items") val items: List<InvoiceItemDto>,
    @SerializedName("userId") val userId: String,
    @SerializedName("document") val document: String
)

fun InvoiceDto.toInvoice() = Invoice(
    id = id,
    amount = amount,
    status = status,
    items = items.map { it.toItem() },
    userId = userId,
    document = document
)