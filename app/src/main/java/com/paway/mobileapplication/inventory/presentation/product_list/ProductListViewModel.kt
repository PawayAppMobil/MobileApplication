package com.paway.mobileapplication.inventory.presentation.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.domain.use_case.SearchProductsUseCase
import kotlinx.coroutines.flow.*

class ProductListViewModel(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        searchProducts("")
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        searchProducts(query)
    }

    private fun searchProducts(query: String) {
        searchProductsUseCase(query).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ProductListState(products = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = ProductListState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = ProductListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    companion object {
        fun provideFactory(
            searchProductsUseCase: SearchProductsUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductListViewModel(searchProductsUseCase) as T
            }
        }
    }
}