package com.paway.mobileapplication.inventory.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.launch
import java.util.UUID
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.paway.mobileapplication.inventory.data.remote.ProductCreate
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class ProductAddViewModel(private val repository: ProductRepository, private val userId: String) : ViewModel() {

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _productCreateResponse = MutableLiveData<Response<ProductCreate>>()
    val productCreateResponse: LiveData<Response<ProductCreate>> get() = _productCreateResponse

    fun addProduct(
        description: String,
        price: Double,
        productName: String,
        stock: Int,
        providerId: String,
        imageFile: File
    ) {
        viewModelScope.launch {
            try {
                // Llamamos al repositorio para crear el producto
                val response = repository.createProduct(
                    description,
                    userId,
                    price,
                    productName,
                    stock,
                    providerId,
                    imageFile
                )

                // Almacenamos la respuesta para actualizar la UI
                _productCreateResponse.postValue(response)
            } catch (e: Exception) {
                // En caso de error, mostramos el mensaje de error
                _productCreateResponse.postValue(Response.error(500, ResponseBody.create(null, "Error: ${e.message}")))
            }
        }
    }
}