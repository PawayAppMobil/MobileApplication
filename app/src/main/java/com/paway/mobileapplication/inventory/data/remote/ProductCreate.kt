package com.paway.mobileapplication.inventory.data.remote


import okhttp3.MultipartBody

data class ProductCreate(
    val userId: String,
    val description: String,
    val price: Double,
    val productName: String,
    val stock: Int,
    val providerId: String,
    val image: MultipartBody.Part // Aquí se maneja la imagen como Multipart
)

