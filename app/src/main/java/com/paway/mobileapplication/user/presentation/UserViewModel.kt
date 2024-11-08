package com.paway.mobileapplication.user.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.user.data.repository.UserRepository
import com.paway.mobileapplication.user.data.remote.dto.LoginRequest
import com.paway.mobileapplication.user.data.remote.dto.LoginResponse
import com.paway.mobileapplication.user.data.remote.dto.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    var loginState = mutableStateOf<Boolean>(false)
        private set

    var userId = mutableStateOf<String?>(null)
        private set

    fun login(username: String, password: String, onResult: (Response<LoginResponse>) -> Unit) {
        val loginRequest = LoginRequest(username, password)
        viewModelScope.launch {
            val response = repository.login(loginRequest)
            onResult(response)
            if (response.isSuccessful) {
                loginState.value = true
                getUserId(username)
            } else {
                loginState.value = false
            }
        }
    }

    fun register(id: String, username: String, password: String, onResult: (Response<RegisterRequest>) -> Unit) {
        val registerRequest = RegisterRequest(id, username, password)
        viewModelScope.launch {
            val response = repository.register(registerRequest)
            onResult(response)
        }
    }

    fun getUserId(username: String) {
        viewModelScope.launch {
            val response = repository.getUserId(username)
            if (response.isSuccessful) {
                userId.value = response.body()?.id
            } else {
                userId.value = null
            }
        }
    }
}
class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
