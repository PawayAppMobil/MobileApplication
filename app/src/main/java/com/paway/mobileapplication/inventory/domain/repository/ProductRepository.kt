package com.paway.mobileapplication.inventory.domain.repository

import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun searchProducts(name: String): Flow<Resource<List<Product>>>
    // Add other repository methods (addProduct, updateProduct, getProductById)
}