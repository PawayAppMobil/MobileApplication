package com.paway.mobileapplication.common

import com.google.gson.GsonBuilder
import com.paway.mobileapplication.inventory.common.Constants
import com.paway.mobileapplication.invoces.data.remote.WebService
import com.paway.mobileapplication.reports.data.remote.ReportService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://paway-app-68cea87ac8ef.herokuapp.com/"
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                response
            }
            .build()
    }

    val webService: WebService by lazy {
        retrofit.create(WebService::class.java)
    }
    val reportService: ReportService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ReportService::class.java)
    }
}