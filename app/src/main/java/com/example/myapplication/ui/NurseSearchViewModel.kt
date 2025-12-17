package com.example.myapplication.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.Nurse
import com.example.myapplication.NurseData

data class NurseSearchUiState(
    val query: String = "",
    val results: List<Nurse> = NurseData.nurses.toList()
)

class NurseSearchViewModel : ViewModel() {

    private val _uiState = mutableStateOf(NurseSearchUiState())
    val uiState: State<NurseSearchUiState> = _uiState

    init {
        doSearch("")
    }

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
        // Opcional: buscar en cada cambio
        doSearch(newQuery)
    }

    fun onSearch() {
        doSearch(_uiState.value.query)
    }

    private fun doSearch(q: String) {
        val trimmed = q.trim().lowercase()
        val all = NurseData.nurses
        val results = if (trimmed.isEmpty()) {
            all.toList()
        } else {
            all.filter { nurse ->
                nurse.name.lowercase().contains(trimmed)
                        || nurse.surname.lowercase().contains(trimmed)
                        || nurse.username.lowercase().contains(trimmed)
                        || nurse.email.lowercase().contains(trimmed)
            }
        }

        _uiState.value = _uiState.value.copy(results = results)
    }
}

