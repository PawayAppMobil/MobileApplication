package com.paway.mobileapplication.reports.data.remote.dto.report

data class ReportRequestDto(
    val startDate: String? = null,
    val endDate: String? = null,
    val type: String? = null
)