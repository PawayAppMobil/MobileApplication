package com.paway.mobileapplication.user.data.remote

import com.paway.mobileapplication.user.data.remote.dto.HomeResponse
import retrofit2.Response
import retrofit2.http.GET

interface HomeServiceApi {
    @GET("/api/providers/total-stock")
    suspend fun getProviders(): Response<List<HomeResponse>>



}