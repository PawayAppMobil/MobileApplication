package com.paway.mobileapplication.user.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paway.mobileapplication.user.data.repository.HomeRepository
import com.paway.mobileapplication.user.data.remote.dto.HomeResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private val _providers = MutableLiveData<Response<List<HomeResponse>>?>()
    val providers: LiveData<Response<List<HomeResponse>>?> get() = _providers

    fun fetchProviders() {
        viewModelScope.launch {
            try {
                val response = homeRepository.getProviders()
                _providers.postValue(response)
            } catch (e: Exception) {
                // Handle the exception, e.g., post an error state to LiveData
                _providers.postValue(null) // or handle it in a more sophisticated way
            }
        }
    }
}