package com.paway.mobileapplication.inventory.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductService {


    @PUT("products/{id}/image")
    suspend fun updateProductImage(@Path("id") id: String, @Body image: String): Response<Unit>


    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body productDto: ProductDto): Response<ProductDto>

    @Multipart
    @POST("/products")
    suspend fun createProduct(
        @Part("description") description: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("price") price: RequestBody,
        @Part("productName") productName: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("providerId") providerId: RequestBody
    ): Response<ProductDto>

    @GET("products")
    suspend fun searchProduct(@Query("productName") name: String): Response<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductDto>

    @GET("products/user/{userId}")
    suspend fun getProductsByUserId(@Path("userId") userId: String): Response<List<ProductDto>>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<Unit>




}