import com.paway.mobileapplication.invoces.domain.model.invoice.InvoiceItem
import java.util.Date

data class InvoiceDTO(
    val date: Date,
    val amount: Double,
    val status: String,
    val productIds: List<String>,  // Lista de IDs de productos
    val userId: String,
    val dueDate: Date
    // No incluimos document aquí ya que se manejará por separado
)