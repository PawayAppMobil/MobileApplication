package com.paway.mobileapplication.inventory.data.repository

import android.util.Log
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.data.local.ProductDao
import com.paway.mobileapplication.inventory.data.local.ProductEntity
import com.paway.mobileapplication.inventory.data.remote.ProductCreate
import com.paway.mobileapplication.inventory.data.remote.ProductDto
import com.paway.mobileapplication.inventory.data.remote.ProductService
import com.paway.mobileapplication.inventory.data.remote.ProductUpdateRequest
import com.paway.mobileapplication.inventory.data.remote.ProductUpdateResponse
import com.paway.mobileapplication.inventory.data.remote.toProduct
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class ProductRepository(
    private val productService: ProductService,
    private val productDao: ProductDao,
) {

    private fun createImagePart(file: File, partName: String = "image"): MultipartBody.Part {
        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private suspend fun getInitialStock(id: String): Int = withContext(Dispatchers.IO) {
        productDao.fetchProductById(id)?.initialStock ?: 0
    }

    suspend fun getAllProductsByUserId(userId: String): Resource<List<Product>> = withContext(Dispatchers.IO) {
        val response = productService.getProductsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { productsDto ->
                val products = productsDto.map { productDto ->
                    val product = productDto.toProduct()
                    val initialStock = getInitialStock(product.id)
                    product.copy(initialStock = initialStock)
                }
                return@withContext Resource.Success(data = products)
            }
            return@withContext Resource.Error(message = "An error occurred")
        }
        return@withContext Resource.Error(message = response.message())
    }

    suspend fun searchProduct(name: String, userId: String): Resource<List<Product>> = withContext(Dispatchers.IO) {
        val response = productService.getProductsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { productsDto ->
                val products = productsDto.filter { it.productName.contains(name, ignoreCase = true) }
                    .map { it.toProduct() }
                return@withContext Resource.Success(data = products)
            }
            return@withContext Resource.Error(message = "An error occurred")
        }
        return@withContext Resource.Error(message = response.message())
    }

    suspend fun insertProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.insert(ProductEntity(product.id, product.productName,product.userId,product.stock, product.stock))
    }

    suspend fun deleteProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.delete(ProductEntity(product.id, product.productName,product.userId,product.stock,product.stock))
    }

    suspend fun getProductById(id: String): Resource<Product> = withContext(Dispatchers.IO) {
        try {
            val response = productService.getProductById(id)
            if (response.isSuccessful) {
                response.body()?.let { productDto ->
                    val product = productDto.toProduct()
                    return@withContext Resource.Success(data = product)
                }
                return@withContext Resource.Error(message = "Product not found")
            }
            return@withContext Resource.Error(message = response.message())
        } catch (e: Exception) {
            Log.e("ProductRepository", "Exception fetching product: ${e.message}", e)
            return@withContext Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }



    suspend fun createProduct(
        description: String,
        userId: String,
        price: Double,
        productName: String,
        stock: Int,
        providerId: String,
        imageFile: File // El archivo de imagen
    ): Response<ProductCreate> {
        // Ejecutar en el hilo IO
        return withContext(Dispatchers.IO) {
            // Convertir los parámetros a RequestBody
            val descriptionRequest = RequestBody.create(MediaType.parse("text/plain"), description)
            val userIdRequest = RequestBody.create(MediaType.parse("text/plain"), userId)
            val priceRequest = RequestBody.create(MediaType.parse("text/plain"), price.toString())
            val productNameRequest = RequestBody.create(MediaType.parse("text/plain"), productName)
            val stockRequest = RequestBody.create(MediaType.parse("text/plain"), stock.toString())
            val providerIdRequest = RequestBody.create(MediaType.parse("text/plain"), providerId)

            // Convertir el archivo de imagen en MultipartBody.Part
            val imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile)
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

            // Llamar al servicio para crear el producto
            productService.createProduct(
                descriptionRequest,
                userIdRequest,
                priceRequest,
                productNameRequest,
                stockRequest,
                providerIdRequest,
                imagePart
            )
        }
    }

    // Actualizar solo la imagen del producto en la API
    suspend fun updateProductImage(id: String, imageFile: File): Response<ProductUpdateResponse> {
        return withContext(Dispatchers.IO) {
            val imagePart = createImagePart(imageFile)
            productService.updateProductImage(id, imagePart)
        }
    }

    // Actualizar los datos del producto en la API
    suspend fun updateProduct(
        id: String,
        productUpdateRequest: ProductUpdateRequest
    ): Response<ProductUpdateResponse> {
        return withContext(Dispatchers.IO) {
            productService.updateProduct(id, productUpdateRequest)
        }

    }




    suspend fun deleteProductFull(product: Product): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = productService.deleteProduct(product.id)
            if (response.isSuccessful) {
                productDao.delete(ProductEntity(product.id, product.productName, product.userId, product.stock, product.initialStock))
                return@withContext Resource.Success(Unit)
            }
            return@withContext Resource.Error(message = response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }

}