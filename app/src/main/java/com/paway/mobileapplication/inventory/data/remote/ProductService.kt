package com.paway.mobileapplication.inventory.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("products?productName={productName}")
    suspend fun searchProduct(@Path("productName") name: String): Response<ResponseDto>

}