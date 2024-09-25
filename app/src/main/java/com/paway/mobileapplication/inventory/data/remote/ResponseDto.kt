package com.paway.mobileapplication.inventory.data.remote

import com.google.gson.annotations.SerializedName

class ResponseDto (
    val response: String,
    @SerializedName("results")
    val products: List<ProductDto>?,
    val error: String?
)