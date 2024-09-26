package com.paway.mobileapplication.inventory.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    suspend fun searchProduct(@Query("productName") name: String): Response<List<ProductDto>>

}