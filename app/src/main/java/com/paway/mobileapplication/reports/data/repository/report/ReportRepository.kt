package com.paway.mobileapplication.reports.data.repository.report

import com.paway.mobileapplication.common.Resource
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

    // Obtener un reporte por ID de usuario
    suspend fun getReportByUserId(userId: String): Resource<List<Report>> = withContext(Dispatchers.IO) {
        try {
            val response = reportService.getReportByUserId(userId)
            if (response.isSuccessful) {
                response.body()?.let { reportDtos ->
                    val reports = reportDtos.map { it.toReport() }
                    return@withContext Resource.Success(data = reports)
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    // Crear un nuevo reporte
    suspend fun createReport(reportRequest: ReportRequestDto): Resource<Report> = withContext(Dispatchers.IO) {
        try {
            val response = reportService.createReport(reportRequest)
            if (response.isSuccessful) {
                response.body()?.let { reportDto ->
                    return@withContext Resource.Success(data = reportDto.toReport())
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }

    // Otros métodos relacionados con reportes se pueden agregar aquí según sea necesario
    suspend fun createTransaction(transactionDto: TransactionDto): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = reportService.createTransaction(transactionDto)
                if (response.isSuccessful) {
                    Resource.Success(Unit)
                } else {
                    // Captura el mensaje de error
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Resource.Error("Error en la creación de la transacción: $errorMessage")
                }
            } catch (e: Exception) {
                Resource.Error("Excepción: ${e.message}")
            }
        }
    }


    suspend fun getTransactionsByUserId(userId: String): Resource<List<Transaction>> = withContext(Dispatchers.IO) {
        try {
            val response = reportService.getTransactionsByUserId()
            if (response.isSuccessful) {
                response.body()?.let { transactionDtos ->
                    val transactions = transactionDtos.map { it.toTransaction() }
                    return@withContext Resource.Success(data = transactions)
                }
                return@withContext Resource.Error("Empty response body")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An error occurred")
        }
    }
}