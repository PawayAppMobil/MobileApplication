package com.paway.mobileapplication.user.data.repository

import com.paway.mobileapplication.user.data.remote.UserServiceApi
import com.paway.mobileapplication.user.data.remote.dto.LoginRequest
import com.paway.mobileapplication.user.data.remote.dto.LoginResponse
import com.paway.mobileapplication.user.data.remote.dto.RegisterRequest
import retrofit2.Response


class UserRepository(private val userServiceApi: UserServiceApi) {

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return userServiceApi.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): Response<RegisterRequest> {
        return userServiceApi.register(registerRequest)
    }

    suspend fun getUserId(username: String): Response<RegisterRequest> {
        return userServiceApi.getUser(username)
    }
}