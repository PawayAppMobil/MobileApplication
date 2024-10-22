package com.paway.mobileapplication.common

import com.google.gson.GsonBuilder
import com.paway.mobileapplication.data.remote.ReportService
import com.paway.mobileapplication.data.remote.WebService
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

    val reportService: ReportService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ReportService::class.java)
    }


}