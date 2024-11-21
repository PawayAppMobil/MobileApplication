package com.paway.mobileapplication.inventory.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductService {


    @Multipart
    @PUT("products/{id}/image")
    suspend fun updateProductImage(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<ProductUpdateResponse>

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Body product: ProductUpdateRequest
    ): Response<ProductUpdateResponse>

    @Multipart
    @POST("products")
    suspend fun createProduct(
        @Part("description") description: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("price") price: RequestBody,
        @Part("productName") productName: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("providerId") providerId: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ProductCreate>

    @GET("products")
    suspend fun searchProduct(@Query("productName") name: String): Response<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductDto>

    @GET("products/user/{userId}")
    suspend fun getProductsByUserId(@Path("userId") userId: String): Response<List<ProductDto>>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<Unit>




}