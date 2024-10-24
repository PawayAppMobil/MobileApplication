package com.paway.mobileapplication.invoces.domain.model.transaction

import java.util.Date

data class Transaction(
    val id: String,
    val amount: Double,
    val date: Date,
    val details: String,
    val invoiceId: String,
    val userId: String,
    val type: String,
    val income: Boolean
)
