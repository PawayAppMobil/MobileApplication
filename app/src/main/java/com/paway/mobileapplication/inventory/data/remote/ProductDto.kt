package com.paway.mobileapplication.inventory.data.remote

data class ProductDto(
    val id: String,
    val description: String,
    val price: Int,
    val productName: String,
    val stock: Int
)
