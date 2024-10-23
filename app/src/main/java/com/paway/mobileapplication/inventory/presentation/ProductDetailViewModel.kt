package com.paway.mobileapplication.inventory.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.common.UIState
import com.paway.mobileapplication.inventory.domain.GetProductByIdUseCase
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModel() {

    private val _state = mutableStateOf(UIState<Product>())
    val state: State<UIState<Product>> = _state

    fun getProductById(id: String) {
        Log.d("ProductDetailViewModel", "Fetching product with id: $id")
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            when (val result = getProductByIdUseCase(id)) {
                is Resource.Success -> {
                    Log.d("ProductDetailViewModel", "Product fetched successfully: ${result.data}")
                    _state.value = UIState(data = result.data)
                }
                is Resource.Error -> {
                    Log.e("ProductDetailViewModel", "Error fetching product: ${result.message}")
                    _state.value = UIState(error = result.message ?: "An error occurred")
                }
            }
        }
    }
}