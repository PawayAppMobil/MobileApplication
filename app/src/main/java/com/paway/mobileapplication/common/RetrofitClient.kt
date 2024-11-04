package com.paway.mobileapplication.common

import com.google.gson.GsonBuilder
import com.paway.mobileapplication.invoces.data.remote.WebService
import com.paway.mobileapplication.user.data.remote.HomeServiceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WebService::class.java)
    }
    val homeServiceApi: HomeServiceApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(HomeServiceApi::class.java)
    }
}