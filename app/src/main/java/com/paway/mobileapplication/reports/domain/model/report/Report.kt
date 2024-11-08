package com.paway.mobileapplication.reports.domain.model.report

import com.paway.mobileapplication.reports.data.remote.dto.report.DateRange
import java.util.Date

data class Report(
    val id: String,
    val userId: String,
    val dateRange: DateRange,
    val generatedAt: String,
    val reportType: String,
    val totalIncome: Double,
    val totalExpenses: Double,
    val transactions: List<Transaction>
)