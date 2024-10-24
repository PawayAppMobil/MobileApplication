package com.paway.mobileapplication.reports.data.remote.dto.report

import com.paway.mobileapplication.reports.domain.model.report.Transaction

data class TransactionDto(
    val id: String,
    val amount: Double,
    val date: String,
    val details: String,
    val invoiceId: String,
    val userId: String,
    val type: String,
    val income: Boolean,
)

fun TransactionDto.toTransaction(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        date = date,
        details = details,
        invoiceId = invoiceId,
        userId = userId,
        type = type,
        income = income
    )
}