package com.example.myapplication

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface NurseService {
    @GET("nurse/index")
    fun listarNurses(): Call<List<Nurse>>
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