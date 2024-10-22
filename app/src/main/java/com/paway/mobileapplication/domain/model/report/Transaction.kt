package com.paway.mobileapplication.domain.model.report

import com.paway.mobileapplication.data.remote.dto.report.TransactionDto
import java.util.Date

data class Transaction(
    val id: String,
    val amount: Double,
    val date: String,
    val details: String,
    val invoiceId: String,
    val userId: String,
    val type: String,
    val income: Boolean,
)
