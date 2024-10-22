package com.paway.mobileapplication.domain.model.report

import com.paway.mobileapplication.data.remote.dto.report.DateRange
import java.util.Date

data class Report(
    val id: String,
    val userId: String,
    val dateRange: DateRange,
    val generatedAt: Date,
    val reportType: String,
    val totalIncome: Double,
    val totalExpenses: Double,
    val transactions: List<Transaction>
)