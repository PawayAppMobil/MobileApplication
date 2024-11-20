package com.paway.mobileapplication.inventory.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.launch
import java.util.UUID
import android.util.Log

class ProductAddViewModel(private val repository: ProductRepository, private val userId: String) : ViewModel() {

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun addProduct(name: String, description: String, price: Double, stock: Int, providerId: String, image: String, onProductAdded: () -> Unit) {
        viewModelScope.launch {
            try {


                val product = Product(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    description = description,
                    price = price,
                    productName = name,
                    stock = stock,
                    image =image,
                    providerId = providerId
                )


                val result = repository.createProduct(product)

                if (result is Resource.Success) {
                    onProductAdded()
                }
            } catch (e: IllegalArgumentException) {
                Log.e("ProductAddViewModel", "Invalid image format: ${e.message}")
                _errorMessage.value = "Invalid image format"
            }
        }
    }
}