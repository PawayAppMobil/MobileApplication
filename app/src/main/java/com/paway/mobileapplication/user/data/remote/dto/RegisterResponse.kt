package com.paway.mobileapplication.user.data.remote.dto

data class RegisterResponse(
    val id: String,
    val username: String,
    val password: String // Si necesitas este campo, o puedes omitirlo
)
