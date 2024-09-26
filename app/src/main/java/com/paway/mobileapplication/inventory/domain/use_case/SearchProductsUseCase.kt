package com.paway.mobileapplication.inventory.domain.use_case

import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.domain.Product
import com.paway.mobileapplication.inventory.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class SearchProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(name: String): Flow<Resource<List<Product>>> {
        return repository.searchProducts(name)
    }
}