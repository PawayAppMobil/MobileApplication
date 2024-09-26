package com.paway.mobileapplication.data.remote

import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.inventory.data.remote.ProductApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }
}