package com.paway.mobileapplication.inventory.domain

data class Product(
    val id: String,
    val userId: String,
    val description: String,
    val price: Double,
    val productName: String,
    val stock: Int,
    val image: String,
    var isFavorite: Boolean = false
)
