package com.paway.mobileapplication.invoces.data.remote.dto.invoice
data class Product(
    val id: String,
    val name:String,
    val stock: Int,
    var isFavorite: Boolean = false
)

data class ProductDTO(
    val id: String,
    val name: String,
    val stock: Int,
    var isFavorite: Boolean = false
)

fun Product.toProductDTO(): ProductDTO {
    return ProductDTO(
        id = this.id,
        name = this.name,
        stock = this.stock,
        isFavorite = this.isFavorite
    )
}
