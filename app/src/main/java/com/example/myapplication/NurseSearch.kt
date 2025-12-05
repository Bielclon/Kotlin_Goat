package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NurseSearch(navController: NavController) {
    // Usamos la lista compartida de enfermeros
    val allNurses = NurseData.nurses

    var query by remember { mutableStateOf("") }
    var results: List<Nurse> by remember { mutableStateOf(allNurses) }

    fun doSearch(q: String) {
        val trimmed = q.trim().lowercase()
        results = if (trimmed.isEmpty()) {
            allNurses
        } else {
            allNurses.filter { nurse ->
                nurse.name.lowercase().contains(trimmed)
                        || nurse.surname.lowercase().contains(trimmed)
                        || nurse.username.lowercase().contains(trimmed)
                        || nurse.email.lowercase().contains(trimmed)
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(text = "Buscar enfermero") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { doSearch(query) })
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { doSearch(query) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (results.isEmpty()) {
            Text(text = "No se encontraron resultados")
        } else {
            LazyColumn {
                items(results) { nurse ->
                    NurseItem(nurse = nurse)
                }
            }
        }
    }
}
