package com.paway.mobileapplication.data.remote.dto.report

import com.paway.mobileapplication.domain.model.report.Report
import java.util.Date

data class ReportDto(
    val id: String,
    val userId: String,
    val dateRange: DateRange, // Cambiar a DateRange
    val generatedAt: Date,
    val reportType: String,
    val totalIncome: Double,
    val totalExpenses: Double,
    val transactions: List<TransactionDto>
)

data class DateRange(
    val startDate: String,
    val endDate: String
)

fun ReportDto.toReport(): Report {
    return Report(
        id = id,
        userId = userId,
        dateRange = DateRange(
            startDate = dateRange.startDate,
            endDate = dateRange.endDate
        ),
        generatedAt = generatedAt,
        reportType = reportType,
        totalIncome = totalIncome,
        totalExpenses = totalExpenses,
        transactions = transactions.map { it.toTransaction() }
    )
}