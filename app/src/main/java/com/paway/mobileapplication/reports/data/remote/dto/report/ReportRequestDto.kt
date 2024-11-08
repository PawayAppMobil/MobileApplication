package com.paway.mobileapplication.reports.data.remote.dto.report

data class ReportRequestDto(
    val userId: String,
    val reportType: String,
    val dateRange: DateRange
)

