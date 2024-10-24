package com.paway.mobileapplication.reports.data.remote

import com.paway.mobileapplication.reports.data.remote.dto.report.ReportDto
import com.paway.mobileapplication.reports.data.remote.dto.report.ReportRequestDto
import com.paway.mobileapplication.reports.data.remote.dto.report.TransactionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReportService {

    @POST("/api/report")
    suspend fun createReport(@Body reportRequest: ReportRequestDto): Response<ReportDto>

    @POST("/transactions")
    suspend fun createTransaction(@Body transactionDto: TransactionDto): Response<Unit>

    @GET("/api/report/{userId}")
    suspend fun getReportByUserId(
        @Query("userId") userId: String,
    ): Response<List<ReportDto>>

    @GET("/transactions")
    suspend fun getTransactionsByUserId(
    ): Response<List<TransactionDto>>
}