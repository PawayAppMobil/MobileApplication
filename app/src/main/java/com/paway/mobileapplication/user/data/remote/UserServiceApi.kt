package com.paway.mobileapplication.user.data.remote

import com.paway.mobileapplication.user.data.remote.dto.LoginRequest
import com.paway.mobileapplication.user.data.remote.dto.LoginResponse
import com.paway.mobileapplication.user.data.remote.dto.RegisterRequest
import com.paway.mobileapplication.user.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserServiceApi {

    @POST("/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @POST("/users")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterRequest>

    @GET("/users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<RegisterRequest>
}