
package com.paway.mobileapplication.inventory.data.remote

import com.paway.mobileapplication.inventory.domain.Product

data class ProductDto(
    val id: String,
    val userId: String?,
    val description: String,
    val price: Double,
    val productName: String,
    val stock: Int,
    val image: String,
    val providerId: String
)

fun ProductDto.toProduct(): Product {
    val userId = this.userId ?: "0"
    return Product(id, userId, description, price, productName, stock, image,providerId,stock)
}