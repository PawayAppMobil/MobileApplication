package com.paway.mobileapplication.invoces.data.remote.dto.transaction

import com.paway.mobileapplication.invoces.domain.model.transaction.Transaction
import java.util.Date

data class TransactionRequestDto(
    val id: String,
    val amount: Double,
    val date: Date,
    val details: String,
    val invoiceId: String,
    val userId: String,
    val type: String,
    val income: Boolean
)

fun Transaction.toTransactionRequestDto() = TransactionRequestDto(
    id = id,
    amount = amount,
    date = date,
    details = details,
    invoiceId = invoiceId,
    userId = userId,
    type = type,
    income = income
)
