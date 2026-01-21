package com.example.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NurseViewModel : ViewModel() {

    private val _nurses = mutableStateOf<List<Nurse>>(emptyList())
    val nurses: State<List<Nurse>> = _nurses

    init {
        fetchNurses()
    }

    private fun fetchNurses() {
        val service = RetrofitClient.instance

        service.listarNurses().enqueue(object : Callback<List<Nurse>> {
            override fun onResponse(call: Call<List<Nurse>>, response: Response<List<Nurse>>) {
                if (response.isSuccessful) {
                    _nurses.value = response.body() ?: emptyList()
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Nurse>>, t: Throwable) {
                Log.e("API", "Error conexión: ${t.message}")
            }
        })
    }
}