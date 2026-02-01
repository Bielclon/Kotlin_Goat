package com.example.myapplication

// AuthModels.kt

// Lo que enviamos para hacer Login

// Lo que enviamos para el Registro
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    // El PDF menciona "imágenes de perfil", ponlo opcional por ahora si no es obligatorio
    val photoUrl: String? = null
)

// Lo que el servidor nos responde (debe incluir el Token)
data class AuthResponse(
    val token: String,
    val user: UserData? // Si el backend devuelve datos del usuario al loguearse
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val photoUrl: String?
)