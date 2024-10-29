package com.paway.mobileapplication.reports.presentation.reports

import com.paway.mobileapplication.reports.data.repository.report.ReportRepository
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.common.Resource
import com.paway.mobileapplication.common.RetrofitClient.reportService
import com.paway.mobileapplication.reports.data.remote.dto.report.DateRange
import com.paway.mobileapplication.reports.data.remote.dto.report.ReportDto
import com.paway.mobileapplication.reports.data.remote.dto.report.ReportRequestDto
import com.paway.mobileapplication.reports.data.remote.dto.report.TransactionDto
import com.paway.mobileapplication.reports.domain.model.report.Report
import com.paway.mobileapplication.reports.domain.model.report.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    private val _reportFlow = MutableStateFlow<Resource<List<Report>>>(Resource.Success(emptyList()))
    val reportFlow: StateFlow<Resource<List<Report>>> get() = _reportFlow

    init {
        fetchReports("12345678")
    }

    private fun fetchReports(userId: String) {
        viewModelScope.launch {
            val result = reportRepository.getReportByUserId(userId)
            _reportFlow.value = result
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

            _reportFlow.value = when (result) {
                is Resource.Success -> {
                    val currentReports = (_reportFlow.value as? Resource.Success)?.data ?: emptyList()
                    result.data?.let {
                        Resource.Success(currentReports + it)
                    } ?: Resource.Error("Error: el reporte no contiene datos.")
                }
                is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
                //else -> Resource.Loading()
            }
        }
    }

    fun deleteReport(userId: String, reportId: String) {
        viewModelScope.launch {
            val result = reportRepository.deleteReport(userId, reportId)
            if (result is Resource.Success) {
                // Filtra el reporte eliminado de la lista
                val updatedReports = (_reportFlow.value as? Resource.Success)?.data?.filter { it.id != reportId }
                _reportFlow.value = Resource.Success(updatedReports ?: emptyList())
            } else {
                _reportFlow.value = Resource.Error("Error al eliminar el reporte.")
            }
        }
    }

}