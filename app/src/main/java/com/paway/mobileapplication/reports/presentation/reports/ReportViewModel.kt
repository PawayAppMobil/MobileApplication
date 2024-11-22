package com.paway.mobileapplication.reports.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.common.Resource
import com.paway.mobileapplication.reports.data.remote.dto.report.DateRange
import com.paway.mobileapplication.reports.data.remote.dto.report.ReportRequestDto
import com.paway.mobileapplication.reports.data.repository.report.ReportRepository
import com.paway.mobileapplication.reports.domain.model.report.Report
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.EOFException

class ReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    private val _reportFlow = MutableStateFlow<Resource<List<Report>>>(Resource.Success(emptyList()))
    val reportFlow: StateFlow<Resource<List<Report>>> get() = _reportFlow

    private val _localReportFlow = MutableStateFlow<List<Report>>(emptyList())
    val localReportFlow: StateFlow<List<Report>> get() = _localReportFlow

    private var userId: String = ""

    fun initialize(id: String) {
        userId = id
        fetchReports(userId)
    }

    private fun fetchReports(userId: String) {
        viewModelScope.launch {
            try {
                val result = reportRepository.getReportByUserId(userId)
                if (result is Resource.Success && result.data != null) {
                    _reportFlow.value = Resource.Success(result.data)
                } else {
                    _reportFlow.value = Resource.Success(emptyList())
                }
            } catch (e: EOFException) {
                _reportFlow.value = Resource.Success(emptyList())
            } catch (e: Exception) {
                _reportFlow.value = Resource.Error("Error al cargar reportes: ${e.message}")
            }
        }
    }

    fun createReport(userId: String, reportType: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            val reportRequest = ReportRequestDto(
                userId = userId,
                reportType = reportType,
                dateRange = DateRange(startDate, endDate)
            )
            val result = reportRepository.createReport(reportRequest)

            if (result is Resource.Success && result.data != null) {
                // Verifica si el reporte ya existe en la lista local antes de agregarlo
                val currentReports = _localReportFlow.value
                if (currentReports.none { it.id == result.data.id }) {
                    _localReportFlow.value = currentReports + result.data
                }
            }
        }
    }

    fun deleteReport(userId: String, reportId: String) {
        viewModelScope.launch {
            val result = reportRepository.deleteReport(userId, reportId)
            if (result is Resource.Success) {
                val updatedReports = _localReportFlow.value.filter { it.id != reportId }
                _localReportFlow.value = updatedReports
            }
        }
    }
}