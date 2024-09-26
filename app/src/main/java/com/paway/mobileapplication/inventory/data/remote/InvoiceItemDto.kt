package com.paway.mobileapplication.inventory.data.remote

import com.google.gson.annotations.SerializedName

data class InvoiceItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("unitPrice") val unitPrice: Int,
    @SerializedName("productId") val productId: String
)