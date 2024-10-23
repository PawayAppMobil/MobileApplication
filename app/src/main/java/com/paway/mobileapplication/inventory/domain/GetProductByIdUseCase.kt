package com.paway.mobileapplication.inventory.domain

import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.data.repository.ProductRepository

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: String): Resource<Product> {
        return repository.getProductById(id)
    }
}