package com.paway.mobileapplication.inventory.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.common.UIState
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.launch
class ProductListViewModel(
    private val repository: ProductRepository,
    private val userId: String
) : ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _state = mutableStateOf(UIState<List<Product>>())
    val state: State<UIState<List<Product>>> get() = _state

    init {
        getAllProductsByUserId()
    }

    private fun getAllProductsByUserId() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getAllProductsByUserId(userId)
            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun refreshProducts() {
        getAllProductsByUserId()
    }

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun searchProduct() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.searchProduct(_name.value, userId)
            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }



    fun filterProductsByName() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getAllProductsByUserId(userId)
            if (result is Resource.Success) {
                val sortedProducts = result.data?.sortedBy { it.productName }
                _state.value = UIState(data = sortedProducts)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun filterProductsByStock() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getAllProductsByUserId(userId)
            if (result is Resource.Success) {
                val sortedProducts = result.data?.sortedBy { it.stock }
                _state.value = UIState(data = sortedProducts)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }



    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProductFull(product)
            refreshProducts()
        }
    }

}