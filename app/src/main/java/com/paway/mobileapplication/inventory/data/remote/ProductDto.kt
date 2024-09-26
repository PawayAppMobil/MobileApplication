package com.paway.mobileapplication.inventory.data.remote

import com.paway.mobileapplication.inventory.domain.Product

data class ProductDto(
    val id: String,
    val description: String,
    val price: Int,
    val productName: String,
    val stock: Int
)
fun ProductDto.toProduct() = Product(id, productName, stock)
