package com.paway.mobileapplication.inventory.data.remote

import retrofit2.http.*

interface ProductService {

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductDto

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body product: ProductDto): ProductDto

    @GET("products")
    suspend fun getAllProducts(): List<ProductDto>

    @POST("products")
    suspend fun createProduct(@Body product: ProductDto): ProductDto

    @DELETE("products")
    suspend fun deleteAllProducts()

    @DELETE("products/{id}")
    suspend fun deleteProductById(@Path("id") id: String)
}