package com.paway.mobileapplication.user.data.repository

import com.paway.mobileapplication.user.data.remote.HomeServiceApi
import com.paway.mobileapplication.user.data.remote.dto.HomeResponse
import retrofit2.Response

class HomeRepository(private val homeServiceApi: HomeServiceApi) {
    suspend fun getProviders(): Response<List<HomeResponse>> {
        return homeServiceApi.getProviders()
    }

}