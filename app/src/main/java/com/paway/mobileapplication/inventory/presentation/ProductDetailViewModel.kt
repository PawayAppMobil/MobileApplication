package com.paway.mobileapplication.inventory.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.inventory.common.Resource
import com.paway.mobileapplication.inventory.common.UIState
import com.paway.mobileapplication.inventory.data.remote.ProductCreate
import com.paway.mobileapplication.inventory.data.remote.ProductUpdateRequest
import com.paway.mobileapplication.inventory.data.remote.ProductUpdateResponse
import com.paway.mobileapplication.inventory.data.repository.ProductRepository
import com.paway.mobileapplication.inventory.domain.GetProductByIdUseCase
import com.paway.mobileapplication.inventory.domain.Product
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
class ProductDetailViewModel(
    private val repository: ProductRepository,
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModel() {

    private val _state = mutableStateOf(UIState<Product>())
    val state: State<UIState<Product>> = _state

    // Obtener el producto por ID
    fun getProductById(id: String) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            when (val result = getProductByIdUseCase(id)) {
                is Resource.Success -> {
                    _state.value = UIState(data = result.data)
                }
                is Resource.Error -> {
                    _state.value = UIState(error = result.message ?: "An error occurred")
                }
            }
        }
    }

    // Actualizar producto e imagen
    fun updateProduct(
        id: String,
        description: String,
        price: Double,
        productName: String,
        stock: Int,
        providerId: String,
        imageFile: File,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Actualizar los detalles del producto
                val response = repository.updateProduct(
                    id,
                    ProductUpdateRequest(
                        description = description,
                        price = price,
                        productName = productName,
                        stock = stock,
                        providerId = providerId
                    )
                )

                // Si la respuesta de actualización del producto fue exitosa, actualizar la imagen
                if (response.isSuccessful) {
                    val responseImage = repository.updateProductImage(id, imageFile)

                    // Llamar a la función de éxito si todo fue exitoso
                    if (responseImage.isSuccessful) {
                        onSuccess() // Llamar a onSuccess
                    } else {
                        onError() // Llamar a onError
                    }
                } else {
                    onError() // Llamar a onError
                }

            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error updating product", e)
                onError() // Si hubo un error, se llama a onError
            }
        }
    }
}
