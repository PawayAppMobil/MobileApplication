package com.paway.mobileapplication.inventory.data.repository

import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.data.local.ProductDao
import com.paway.mobileapplication.inventory.data.local.ProductEntity
import com.paway.mobileapplication.inventory.data.remote.ProductDto
import com.paway.mobileapplication.inventory.data.remote.ProductService
import com.paway.mobileapplication.inventory.data.remote.toProduct
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productService: ProductService,
    private val productDao: ProductDao,
) {

    private suspend fun isFavorite(id: String): Boolean = withContext(Dispatchers.IO) {
        productDao.fetchProductById(id)?.let {
            return@withContext true
        }
        return@withContext false
    }


    suspend fun getAllProducts(): Resource<List<Product>> = withContext(Dispatchers.IO) {
        val response = productService.searchProduct("") // Assuming an empty string returns all products
        if (response.isSuccessful) {
            response.body()?.let { productsDto ->
                val products = mutableListOf<Product>()
                productsDto.forEach { productDto: ProductDto ->
                    val product = productDto.toProduct()
                    product.isFavorite = isFavorite(product.id)
                    products.add(product)
                }
                return@withContext Resource.Success(data = products)
            }
            return@withContext Resource.Error(message = "An error occurred")
        }
        return@withContext Resource.Error(message = response.message())
    }

    suspend fun searchProduct(name: String): Resource<List<Product>> = withContext(Dispatchers.IO) {
        val response = productService.searchProduct(name)
        if (response.isSuccessful) {
            response.body()?.let { productsDto ->
                val products = mutableListOf<Product>()
                productsDto.forEach { productDto: ProductDto ->
                    val product = productDto.toProduct()
                    product.isFavorite = isFavorite(product.id)
                    products.add(product)
                }
                return@withContext Resource.Success(data = products)
            }
            return@withContext Resource.Error(message = "An error occurred")
        }
        return@withContext Resource.Error(message = response.message())
    }

    suspend fun insertProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.insert(ProductEntity(product.id, product.name, product.stock))
    }

    suspend fun deleteProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.delete(ProductEntity(product.id, product.name, product.stock))
    }

    suspend fun getProductById(id: String): Resource<Product> = withContext(Dispatchers.IO) {
        val response = productService.getProductById(id)
        if (response.isSuccessful) {
            response.body()?.let { productDto ->
                val product = productDto.toProduct()
                product.isFavorite = isFavorite(product.id)
                return@withContext Resource.Success(data = product)
            }
            return@withContext Resource.Error(message = "Product not found")
        }
        return@withContext Resource.Error(message = response.message())
    }
}