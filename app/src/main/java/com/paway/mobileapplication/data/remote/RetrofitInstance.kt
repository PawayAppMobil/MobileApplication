package com.paway.mobileapplication.data.remote

import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.remote.ProductApi
import com.paway.mobileapplication.inventory.data.remote.ProductService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductService::class.java)
    }
}