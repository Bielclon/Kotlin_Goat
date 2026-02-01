package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Imports para los estados de Compose
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
// Imports de tus modelos y red
import com.example.myapplication.RetrofitClient
import com.example.myapplication.LoginRequest
import com.example.myapplication.RegisterRequest

// Definición de estados
sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    data class Success(val token: String) : AuthState
    data class Error(val message: String) : AuthState
}

class AuthViewModel : ViewModel() {

    // --- VARIABLES DEL FORMULARIO ---
    // Login y Registro comparten email/password
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Exclusivas de Registro
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var username by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    // --- ESTADO DE LA UI ---
    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    // --- FUNCIÓN LOGIN ---
    fun login() {
        if (email.isBlank() || password.isBlank()) {
            authState = AuthState.Error("Por favor, rellena email y contraseña")
            return
        }

        authState = AuthState.Loading
        viewModelScope.launch {
            try {

                val request = LoginRequest(email = email, password = password)

                val response = RetrofitClient.instance.login(request)

                if (response.isSuccessful && response.body() != null) {
                    authState = AuthState.Success(response.body()!!.token)
                } else {
                    authState = AuthState.Error("Login fallido: ${response.code()}")
                }
            } catch (e: Exception) {
                authState = AuthState.Error("Fallo de red: ${e.message}")
            }
        }
    }

    // --- FUNCIÓN REGISTER ---
    fun register() {
        // 1. Validaciones locales
        if (password != confirmPassword) {
            authState = AuthState.Error("Las contraseñas no coinciden")
            return
        }
        if (name.isBlank() || surname.isBlank() || username.isBlank() || email.isBlank()) {
            authState = AuthState.Error("Todos los campos son obligatorios")
            return
        }

        authState = AuthState.Loading
        viewModelScope.launch {
            try {
                // 2. CORRECCIÓN IMPORTANTE:
                // Enviamos los datos tal cual, sin unirlos.
                // El backend se encarga de guardarlos en sus columnas.

                val request = RegisterRequest(
                    name = name.trim(),        // Enviamos solo el nombre limpio
                    surname = surname.trim(),  // Enviamos solo el apellido limpio
                    username = username.trim(),
                    email = email.trim(),
                    password = password,
                    photoUrl = null // Opcional
                )

                val response = RetrofitClient.instance.register(request)

                if (response.isSuccessful && response.body() != null) {
                    // Si el registro devuelve token, entramos directo (Success)
                    authState = AuthState.Success(response.body()!!.token)
                } else {
                    // Aquí capturamos el Error 400 (Usuario ya existe)
                    val errorMsg = if (response.code() == 400) "El usuario o email ya existe" else "Error: ${response.code()}"
                    authState = AuthState.Error(errorMsg)
                }
            } catch (e: Exception) {
                authState = AuthState.Error("Fallo de conexión: ${e.message}")
            }
        }
    }
}