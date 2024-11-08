package com.paway.mobileapplication.user.presentation

import com.paway.mobileapplication.user.data.remote.UserServiceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://paway-app-68cea87ac8ef.herokuapp.com/"

    val api: UserServiceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserServiceApi::class.java)
    }
}
