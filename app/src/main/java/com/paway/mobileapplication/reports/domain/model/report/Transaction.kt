package com.paway.mobileapplication.reports.domain.model.report

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