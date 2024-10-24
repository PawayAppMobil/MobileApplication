import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
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
