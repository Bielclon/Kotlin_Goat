package com.example.myapplication

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NurseService {
    @GET("nurse/index")
    fun listarNurses(): Call<List<Nurse>>

    // --- NUEVO: Autenticación ---
    // Nota: Verifica si la ruta en Kotlin_Goat es "login" o "auth/login"

    @POST("nurse/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("nurse/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

}




object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val instance: NurseService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NurseService::class.java)
    }
}