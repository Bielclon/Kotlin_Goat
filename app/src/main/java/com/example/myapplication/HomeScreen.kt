package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.pantalla_inicial), modifier = Modifier.padding(bottom = 32.dp))

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(stringResource(R.string.ir_a_login))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("listAll") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(stringResource(R.string.ir_a_lista_de_enfermeros))
        }

        Button(
            onClick = { navController.navigate("search") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(stringResource(R.string.ir_a_b_squeda))
        }
    }
}