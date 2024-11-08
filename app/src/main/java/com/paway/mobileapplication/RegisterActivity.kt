package com.paway.mobileapplication
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels

import com.paway.mobileapplication.databinding.ActivityRegisterBinding
import com.paway.mobileapplication.user.data.repository.UserRepository
import com.paway.mobileapplication.user.presentation.RetrofitInstance
import com.paway.mobileapplication.user.presentation.UserViewModel
import com.paway.mobileapplication.user.presentation.UserViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class RegisterActivity : ComponentActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val id = binding.dni.text.toString() // Obtén el DNI
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            userViewModel.register(id, username, password) { response ->
                if (response.isSuccessful) {
                    response.body()?.let { registerResponse ->

                        Toast.makeText(this, "Registro exitoso. ID: ${registerResponse.id}", Toast.LENGTH_SHORT).show()


                        finish()
                    }
                } else {
                    // Manejo de error
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.loginPageButton.setOnClickListener {
            finish() // Cierra la actividad de registro y vuelve a login
        }
    }
}
