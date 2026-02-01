package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Estos 3 imports son MÁGICOS para que funcione el "by mutableStateOf"
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
// Tus modelos y retrofit
import com.example.myapplication.RetrofitClient
import com.example.myapplication.LoginRequest
import com.example.myapplication.RegisterRequest

// 1. DEFINIMOS LOS ESTADOS AQUÍ MISMO (Para que no dé error de "Unresolved reference")
sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    data class Success(val token: String) : AuthState
    data class Error(val message: String) : AuthState
}

// 2. LA CLASE VIEWMODEL
class AuthViewModel : ViewModel() {

    // --- VARIABLES DE TEXTO ---
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Variables extra para Registro
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var username by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    // --- ESTADO DE LA APP (Cargando, Error, etc.) ---
    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    // --- LOGIN ---
    fun login() {
        if (email.isBlank() || password.isBlank()) {
            authState = AuthState.Error("Faltan datos")
            return
        }

        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                val request = LoginRequest(email = email, password = password)
                val response = RetrofitClient.instance.login(request) // Asegúrate que tu Retrofit tenga .login

                if (response.isSuccessful && response.body() != null) {
                    authState = AuthState.Success(response.body()!!.token)
                } else {
                    authState = AuthState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                authState = AuthState.Error("Fallo red: ${e.message}")
            }
        }
    }

    // --- REGISTRO ---
    fun register() {
        if (password != confirmPassword) {
            authState = AuthState.Error("Contraseñas no coinciden")
            return
        }

        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                // Unimos nombre y apellidos si el backend pide solo "name"
                val fullName = "$name $surname".trim()

                val request = RegisterRequest(
                    name = fullName,
                    email = email,
                    password = password,
                    photoUrl = null
                )

                val response = RetrofitClient.instance.register(request) // Asegúrate que tu Retrofit tenga .register

                if (response.isSuccessful && response.body() != null) {
                    authState = AuthState.Success(response.body()!!.token)
                } else {
                    authState = AuthState.Error("Error registro: ${response.code()}")
                }
            } catch (e: Exception) {
                authState = AuthState.Error("Fallo red: ${e.message}")
            }
        }
    }
}