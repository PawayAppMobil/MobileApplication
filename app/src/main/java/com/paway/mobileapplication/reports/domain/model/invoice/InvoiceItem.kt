<<<<<<<< HEAD:app/src/main/java/com/paway/mobileapplication/inventory/domain/InvoiceItem.kt
package com.paway.mobileapplication.inventory.domain
========
package com.paway.mobileapplication.reports.domain.model.invoice
>>>>>>>> origin/feature-reportes:app/src/main/java/com/paway/mobileapplication/reports/domain/model/invoice/InvoiceItem.kt

data class InvoiceItem(
    val id: String,
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val productId: String
)
