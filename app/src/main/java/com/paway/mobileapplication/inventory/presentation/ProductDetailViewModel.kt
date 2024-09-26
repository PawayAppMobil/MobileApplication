package com.paway.mobileapplication.inventory.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.launch

class ProductDetailViewModel: ViewModel() {
    private val _product = mutableStateOf(Product("", "", 0))
    val product: State<Product> get() = _product

    private val _stockDesired = mutableStateOf("")
    val stockDesired: State<String> get() = _stockDesired

    fun setProduct(product: Product) {
        _product.value = product
        _stockDesired.value = product.stock.toString()
    }

    fun onStockChanged(newStock: String) {
        _product.value = _product.value.copy(stock = newStock.toIntOrNull() ?: 0)
    }

    fun onStockDesiredChanged(desiredStock: String) {
        _stockDesired.value = desiredStock
    }

    fun saveChanges(onSave: () -> Unit) {
        viewModelScope.launch {
            // Aquí puedes realizar la lógica de guardado en base de datos
            onSave()
        }
    }

    fun cancelChanges(onCancel: () -> Unit) {
        onCancel()
    }
}