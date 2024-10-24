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

class ProductListViewModel(private val repository: ProductRepository): ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _state = mutableStateOf(UIState<List<Product>>())
    val state: State<UIState<List<Product>>> get() = _state

    fun onNameChanged(name: String) {
        _name.value = name
    }

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getAllProducts()
            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun searchProduct(){
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.searchProduct(_name.value)
            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
            } else {
                _state.value = UIState(error = result.message?: "An error occurred")
            }
        }
    }

    fun toggleFavorite(product: Product){
        product.isFavorite = !product.isFavorite
        viewModelScope.launch {
            if (product.isFavorite) {
                repository.insertProduct(product)
            } else {
                repository.deleteProduct(product)
            }
            val heroes = _state.value.data
            _state.value = UIState(data = emptyList())
            _state.value = UIState(data = heroes)
        }
    }

    fun filterProductsByName() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getAllProducts()
            if (result is Resource.Success) {
                val sortedProducts = result.data?.sortedBy { it.name }
                _state.value = UIState(data = sortedProducts)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun filterProductsByStock() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getAllProducts()
            if (result is Resource.Success) {
                val sortedProducts = result.data?.sortedBy { it.stock }
                _state.value = UIState(data = sortedProducts)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }

    fun filterProductsByFavorites() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = repository.getAllProducts()
            if (result is Resource.Success) {
                val favoriteProducts = result.data?.filter { it.isFavorite }
                _state.value = UIState(data = favoriteProducts)
            } else {
                _state.value = UIState(error = result.message ?: "An error occurred")
            }
        }
    }
}