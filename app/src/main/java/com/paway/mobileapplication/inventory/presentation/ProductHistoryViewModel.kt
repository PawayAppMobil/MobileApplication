package com.paway.mobileapplication.inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.ProductHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductHistoryViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProductHistory(emptyList()))
    val state: StateFlow<ProductHistory> = _state

    init {
        viewModelScope.launch {
            repository.productHistory.collect {
                _state.value = it
            }
        }
    }

    fun clearHistory() {
        repository.clearHistory()
    }
}