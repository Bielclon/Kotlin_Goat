package com.example.myapplication.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.Nurse
import com.example.myapplication.NurseItem

@Composable
fun NurseSearch(navController: NavController, viewModel: NurseSearchViewModel = viewModel()) {
    val uiState by viewModel.uiState

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(
            value = uiState.query,
            onValueChange = { viewModel.onQueryChange(it) },
            label = { Text(text = "Buscar enfermero") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { viewModel.onSearch() })
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.onSearch() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.results.isEmpty()) {
            Text(text = "No se encontraron resultados")
        } else {
            LazyColumn {
                items(uiState.results) { nurse ->
                    NurseItem(nurse = nurse)
                }
            }
        }
    }
}
