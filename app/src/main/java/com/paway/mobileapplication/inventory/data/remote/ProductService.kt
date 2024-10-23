package com.paway.mobileapplication.inventory.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    @GET("products")
    suspend fun searchProduct(@Query("productName") name: String): Response<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductDto>

}