package com.paway.mobileapplication.inventory.data.remote


data class ProductUpdateResponse(
    val description: String,
    val price: Double,
    val productName: String,
    val stock: Int,
    val providerId: String,
    val image: String?
)
