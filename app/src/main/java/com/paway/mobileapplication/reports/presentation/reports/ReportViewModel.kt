package com.paway.mobileapplication.reports.presentation.reports

import com.paway.mobileapplication.reports.data.repository.report.ReportRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.reports.data.remote.dto.report.ReportRequestDto
import com.paway.mobileapplication.reports.data.remote.dto.report.TransactionDto
import com.paway.mobileapplication.reports.domain.model.report.Report
import com.paway.mobileapplication.reports.domain.model.report.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportViewModel(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _reportFlow = MutableStateFlow<List<Report>>(emptyList())
    val reportFlow: StateFlow<List<Report>> get() = _reportFlow

    private val _errorFlow = MutableStateFlow<String?>(null)
    val errorFlow: StateFlow<String?> get() = _errorFlow

    private val _transactionFlow = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionFlow: StateFlow<List<Transaction>> get() = _transactionFlow


    init {
        getTransactionsByUserId("23") // Cambia el ID por el real del usuario
    }


    fun getReportByUserId(userId: String) {
        viewModelScope.launch {
            when (val result = reportRepository.getReportByUserId(userId)) {
                is Resource.Success -> {
                    //  para verificar la respuesta del backend
                    Log.d("ReportViewModel", "Respuesta del backend: ${result.data}")


                    _reportFlow.value = result.data ?: emptyList()
                }
                is Resource.Error -> {

                    Log.e("ReportViewModel", "Error al obtener reportes: ${result.message}")
                    _errorFlow.value = result.message
                }
            }
        }
    }


    fun createReport(reportRequest: ReportRequestDto) {
        viewModelScope.launch {
            when (val result = reportRepository.createReport(reportRequest)) {
                is Resource.Success -> {
                    Log.d("ReportViewModel", "Reporte creado exitosamente.")
                }
                is Resource.Error -> {
                    _errorFlow.value = result.message
                }
            }
        }
    }


    fun createTransaction(transactionDto: TransactionDto) {
        viewModelScope.launch {
            when (val result = reportRepository.createTransaction(transactionDto)) {
                is Resource.Success -> {
                    Log.d("ReportViewModel", "Transacción creada exitosamente.")
                    getReportByUserId(transactionDto.userId)
                }
                is Resource.Error -> {
                    Log.e("ReportViewModel", "Error al crear transacción: ${result.message}")
                    _errorFlow.value = result.message
                }
            }
        }
    }
    fun getTransactionsByUserId(userId: String) {
        viewModelScope.launch {
            when (val result = reportRepository.getTransactionsByUserId(userId)) {
                is Resource.Success -> {
                    Log.d("ReportViewModel", "Transacciones obtenidas: ${result.data}")
                    _transactionFlow.value = result.data ?: emptyList()
                }
                is Resource.Error -> {
                    Log.e("ReportViewModel", "Error al obtener transacciones: ${result.message}")
                    _errorFlow.value = result.message
                }
            }
        }
    }
}