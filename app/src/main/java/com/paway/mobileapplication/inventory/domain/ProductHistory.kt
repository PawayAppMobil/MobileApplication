package com.paway.mobileapplication.inventory.domain

data class ProductHistory(
    val products: List<Product>,
    val timestamp: Long = System.currentTimeMillis()
)