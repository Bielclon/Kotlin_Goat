package com.example.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.AuthState
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NurseViewModel : ViewModel() {

    private val _nurses = mutableStateOf<List<Nurse>>(emptyList())
    val nurses: State<List<Nurse>> = _nurses

    private val _loggedInNurseId = mutableStateOf<Long?>(null)
    val loggedInNurseId: State<Long?> = _loggedInNurseId

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

    private fun upsertNurseInList(updated: Nurse) {
        val list = _nurses.value.toMutableList()
        val index = list.indexOfFirst { it.id == updated.id }
        if (index >= 0) {
            list[index] = updated
        } else {
            list.add(updated)
        }
        _nurses.value = list
    }

    private fun removeNurseFromList(id: Long) {
        _nurses.value = _nurses.value.filterNot { it.id == id }
    }

    fun getNurseById(id: Long) {
        val service = RetrofitClient.instance
        service.getNurseById(id).enqueue(object : Callback<Nurse> {
            override fun onResponse(call: Call<Nurse>, response: Response<Nurse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        upsertNurseInList(it)
                    }
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Nurse>, t: Throwable) {
                Log.e("API", "Error conexión: ${t.message}")
            }
        })
    }

    fun updateNurse(id: Long, nurse: Nurse, onResult: (Boolean) -> Unit = {}) {
        val service = RetrofitClient.instance
        service.updateNurse(id, nurse).enqueue(object : Callback<Nurse> {
            override fun onResponse(call: Call<Nurse>, response: Response<Nurse>) {
                val ok = response.isSuccessful
                if (ok) {
                    response.body()?.let {
                        upsertNurseInList(it)
                    }
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
                onResult(ok)
            }

            override fun onFailure(call: Call<Nurse>, t: Throwable) {
                Log.e("API", "Error conexión: ${t.message}")
                onResult(false)
            }
        })
    }

    fun deleteNurse(id: Long, onResult: (Boolean) -> Unit = {}) {
        val service = RetrofitClient.instance
        service.deleteNurse(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val ok = response.isSuccessful
                if (ok) {
                    removeNurseFromList(id)
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
                onResult(ok)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error conexión: ${t.message}")
                onResult(false)
            }
        })
    }

    fun login(emailOrUsername: String, password: String, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        val service = RetrofitClient.instance
        val request = LoginRequest(email = emailOrUsername, username = emailOrUsername, password = password)
        service.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val ok = body?.success == true
                    if (ok) {
                        val user = body.user
                        if (user != null) {
                            upsertNurseInList(user)
                            _loggedInNurseId.value = user.id
                        } else {
                            _loggedInNurseId.value = body?.id
                        }
                    }
                    onResult(ok, body?.message)
                } else {
                    Log.e("API", "Error: ${response.code()}")
                    onResult(false, "Error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("API", "Error conexión: ${t.message}")
                onResult(false, t.message)
            }
        })
    }
}

data class LoginRequest(
    val email: String? = null,
    val username: String? = null,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val id: Long? = null,
    val token: String? = null,
    val message: String? = null,
    val user: Nurse? = null
)
