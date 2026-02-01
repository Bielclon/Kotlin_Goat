package com.example.myapplication.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Nurse
import com.example.myapplication.RetrofitClient
import kotlinx.coroutines.launch

data class NurseSearchUiState(
    val query: String = "",
    val results: List<Nurse> = emptyList(), // Inicializamos vacía
    val isLoading: Boolean = false,
    val error: String? = null
)

class NurseSearchViewModel : ViewModel() {

    private val _uiState = mutableStateOf(NurseSearchUiState())
    val uiState: State<NurseSearchUiState> = _uiState


    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }

    fun onSearch() {
        val query = _uiState.value.query.trim()

        if (query.isEmpty()) {
            _uiState.value = _uiState.value.copy(results = emptyList())
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Llamada al Backend
                val response = RetrofitClient.instance.buscarEnfermeros(query)

                if (response.isSuccessful) {
                    val nurses = response.body() ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        results = nurses,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error al buscar: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Fallo de conexión: ${e.message}"
                )
            }
        }
    }
}
