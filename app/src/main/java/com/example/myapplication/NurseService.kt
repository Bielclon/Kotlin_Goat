package com.example.myapplication

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NurseService {
    @GET("nurse/index")
    fun listarNurses(): Call<List<Nurse>>

    @GET("nurse/{id}")
    fun getNurseById(@Path("id") id: Long): Call<Nurse>

    @PUT("nurse/{id}")
    fun updateNurse(@Path("id") id: Long, @Body nurse: Nurse): Call<Nurse>

    @DELETE("nurse/{id}")
    fun deleteNurse(@Path("id") id: Long): Call<Void>
  
    @POST("nurse/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

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