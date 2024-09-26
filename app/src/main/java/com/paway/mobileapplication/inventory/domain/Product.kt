package com.paway.mobileapplication.inventory.domain

data class Product(
    val id: String,
    val name:String,
    val stock: Int,
    var isFavorite: Boolean = false
)
