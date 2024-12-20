package com.paway.mobileapplication.invoces.data.remote.dto.invoice

import com.paway.mobileapplication.invoces.domain.model.invoice.Invoice
import android.util.Base64
import com.paway.mobileapplication.inventory.domain.Product
import com.google.gson.annotations.SerializedName
import com.paway.mobileapplication.invoces.data.remote.dto.transaction.TransactionResponseDto
import java.text.SimpleDateFormat
import java.util.*

data class CreateInvoiceResponse(
    val invoice: InvoiceResponseDto,
    val transaction: TransactionResponseDto? = null
)

data class InvoiceResponseDto(
    val id: String?,
    @SerializedName("date")
    val dateStr: String,
    val amount: Double,
    val status: String,
    val items: List<Product>? = null,
    val userId: String,
    @SerializedName("dueDate")
    val dueDateStr: String,
    val document: String?
) {

}

fun InvoiceResponseDto.toInvoice(): Invoice {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    return Invoice(
        id = id ?: "",
        date = try {
            dateStr.let { dateFormat.parse(it) } ?: Date()
        } catch (e: Exception) {
            Date()
        },
        amount = amount,
        status = status,
        items = items ?: emptyList(),
        userId = userId,
        dueDate = try {
            dueDateStr.let { dateFormat.parse(it) } ?: Date()
        } catch (e: Exception) {
            Date()
        },
        document = document?.let { Base64.decode(it, Base64.DEFAULT) }
    )
}