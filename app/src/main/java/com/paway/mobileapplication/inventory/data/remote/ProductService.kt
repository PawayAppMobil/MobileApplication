package com.paway.mobileapplication.inventory.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ProductService {



    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body productDto: ProductDto): Response<ProductDto>

    @POST("products")
    suspend fun createProduct(@Body productDto: ProductDto): Response<ProductDto>

    @GET("products")
    suspend fun searchProduct(@Query("productName") name: String): Response<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductDto>

    @GET("products/user/{userId}")
    suspend fun getProductsByUserId(@Path("userId") userId: String): Response<List<ProductDto>>
}