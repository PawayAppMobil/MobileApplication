package com.paway.mobileapplication.inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.launch

class ProductAddViewModel(private val repository: ProductRepository) : ViewModel() {

    fun addProduct(name: String, description: String, price: Double, stock: Int, onProductAdded: () -> Unit) {
        viewModelScope.launch {
            val product = Product(
                id = "123452", // Generate or assign an ID
                productName = name,
                description = description,
                price = price,
                stock = stock,
                userId = "currentUserId", // Replace with actual user ID
                image = "https://example.com/image.jpg" // Replace with actual image URL
            )
            repository.createProduct(product)
            onProductAdded()
        }
    }
}