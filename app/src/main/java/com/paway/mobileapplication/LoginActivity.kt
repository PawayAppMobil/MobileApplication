package com.paway.mobileapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.paway.mobileapplication.databinding.ActivityLoginBinding
import com.paway.mobileapplication.user.data.repository.UserRepository
import com.paway.mobileapplication.user.presentation.RetrofitInstance
import com.paway.mobileapplication.user.presentation.UserViewModel
import com.paway.mobileapplication.user.presentation.UserViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            userViewModel.login(username, password) { response ->
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        val message = loginResponse.message

                        if (message == "Login successful") {
                            // Obtener el ID de usuario en una coroutine
                            GlobalScope.launch {
                                userViewModel.getUserId(username)

                                // Esperar a que se actualice userId
                                val userId = userViewModel.userId.value

                                if (userId != null) {
                                    // Crear el Intent para iniciar MainActivitySelector
                                    val intent = Intent(this@LoginActivity, MainActivitySelector::class.java).apply {
                                        putExtra("USER_ID", userId)
                                    }
                                    startActivity(intent)
                                    finish()
                                } else {
                                    runOnUiThread {
                                        Toast.makeText(this@LoginActivity, "Failed to retrieve user ID", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {

                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registerPageButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}