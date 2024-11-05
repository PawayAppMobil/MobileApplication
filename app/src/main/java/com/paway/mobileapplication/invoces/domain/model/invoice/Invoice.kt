package com.paway.mobileapplication.invoces.domain.model.invoice

import com.paway.mobileapplication.inventory.domain.Product
import java.util.Date

data class Invoice(
    val id: String = "",
    val date: Date = Date(),
    val amount: Double = 0.0,
    val status: String = "",
    val items: List<Product> = listOf(),
    val userId: String = "",
    val dueDate: Date = Date(),
    val document: ByteArray? = null
)