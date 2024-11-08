package com.paway.mobileapplication.reports.data.repository.report

import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.reports.data.remote.ReportService
import com.paway.mobileapplication.reports.data.remote.dto.report.ReportRequestDto
import com.paway.mobileapplication.reports.data.remote.dto.report.TransactionDto
import com.paway.mobileapplication.reports.data.remote.dto.report.toReport
import com.paway.mobileapplication.reports.data.remote.dto.report.toTransaction
import com.paway.mobileapplication.reports.domain.model.report.Report
import com.paway.mobileapplication.reports.domain.model.report.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReportRepository(private val reportService: ReportService) {



    suspend fun getReportByUserId(userId: String): Resource<List<Report>> = withContext(Dispatchers.IO) {
        try {
            val response = reportService.getReportByUserId(userId)
            if (response.isSuccessful) {
                response.body()?.let { reportDtos ->
                    val reports = reportDtos.map { it.toReport() }
                    return@withContext Resource.Success(reports)
                } ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("Error en la respuesta del servidor: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Excepción: ${e.message}")
        }
    }


    suspend fun createReport(reportRequest: ReportRequestDto): Resource<Report> = withContext(Dispatchers.IO) {
        try {
            val response = reportService.createReport(reportRequest)
            if (response.isSuccessful) {
                response.body()?.let { reportDto ->
                    return@withContext Resource.Success(reportDto.toReport())
                } ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("Error en la respuesta del servidor: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Excepción: ${e.message}")
        }
    }

    suspend fun deleteReport(userId: String, reportId: String): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = reportService.deleteReport(userId, reportId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error en la respuesta del servidor: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Excepción: ${e.message}")
        }
    }
}