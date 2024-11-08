package com.paway.mobileapplication.reports.data.remote

import com.paway.mobileapplication.reports.data.remote.dto.report.ReportDto
import com.paway.mobileapplication.reports.data.remote.dto.report.ReportRequestDto
import com.paway.mobileapplication.reports.data.remote.dto.report.TransactionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportService {

    @POST("/reports")
    suspend fun createReport(@Body reportRequest: ReportRequestDto): Response<ReportDto>


    @GET("/reports/user/{userId}")
    suspend fun getReportByUserId(
        @Path("userId") userId: String
    ): Response<List<ReportDto>>

    @GET("/transactions")
    suspend fun getTransactionsByUserId(
    ): Response<List<TransactionDto>>

    @DELETE("/reports/{userId}/{reportId}")
    suspend fun deleteReport(
        @Path("userId") userId: String,
        @Path("reportId") reportId: String
    ): Response<Unit>
}