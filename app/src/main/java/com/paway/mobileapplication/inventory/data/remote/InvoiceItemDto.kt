package com.paway.mobileapplication.inventory.data.remote

import com.google.gson.annotations.SerializedName
import com.paway.mobileapplication.inventory.domain.InvoiceItem

data class InvoiceItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("unitPrice") val unitPrice: Double,
    @SerializedName("productId") val productId: String
)
fun InvoiceItemDto.toItem()= InvoiceItem( id,quantity)