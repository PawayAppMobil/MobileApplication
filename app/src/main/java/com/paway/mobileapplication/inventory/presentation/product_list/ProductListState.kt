package com.paway.mobileapplication.inventory.presentation.product_list

import com.paway.mobileapplication.inventory.domain.Product

data class ProductListState(
    val isLoading: Boolean = false,
    var products: List<Product> = emptyList(),
    val error: String = "",
    val isFavorite: Boolean = false // Asegúrate de que este parámetro esté presente

)