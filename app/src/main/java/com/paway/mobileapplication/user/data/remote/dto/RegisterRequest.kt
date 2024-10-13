package com.paway.mobileapplication.user.data.remote.dto

import com.paway.mobileapplication.user.domain.model.User

data class RegisterRequest(
    val id: String,
    val username: String,
    val password: String
)

fun RegisterRequest.toUser() = User(
    id = id,
    username = username,
    password = password
)