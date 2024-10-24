<<<<<<<< HEAD:app/src/main/java/com/paway/mobileapplication/inventory/domain/InvoiceDTO.kt
import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
========
package com.paway.mobileapplication.reports.domain.model.invoice

>>>>>>>> origin/feature-reportes:app/src/main/java/com/paway/mobileapplication/reports/domain/model/invoice/Invoice.kt
import java.util.Date

data class InvoiceDTO(
    val date: Date,
    val amount: Double,
    val status: String,
    val items: List<InvoiceItem>,
    val transactionId: String,
    val userId: String,
    val dueDate: Date
)
