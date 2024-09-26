package com.paway.mobileapplication.inventory.data.repository

import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.data.remote.ProductApi
import com.paway.mobileapplication.inventory.data.remote.toProduct
import com.paway.mobileapplication.inventory.domain.Product
import com.paway.mobileapplication.inventory.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ProductRepositoryImpl(
    private val api: ProductApi
) : ProductRepository {

    override fun searchProducts(name: String): Flow<Resource<List<Product>>> = flow {
        try {
            emit(Resource.Loading())
            val products = api.searchProducts(name).body()?.map { it.toProduct() } ?: emptyList()
            emit(Resource.Success(products))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    // Implement other repository methods (addProduct, updateProduct, getProductById) similarly
}