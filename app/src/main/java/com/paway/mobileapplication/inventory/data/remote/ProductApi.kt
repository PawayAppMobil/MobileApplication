package com.paway.mobileapplication.inventory.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ProductApi {
    @GET("products")
    suspend fun searchProducts(@Query("productName") name: String): Response<List<ProductDto>>

    @POST("products")
    suspend fun addProduct(@Body product: ProductDto): Response<ProductDto>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body product: ProductDto): Response<ProductDto>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductDto>
}