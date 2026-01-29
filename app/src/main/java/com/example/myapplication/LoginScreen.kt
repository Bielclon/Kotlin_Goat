package com.example.myapplication.ui

import AuthViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel() // 1. Inyectamos nuestro ViewModel
) {
    // 2. Observamos el estado del ViewModel
    val state = viewModel.authState
    val context = LocalContext.current

    // Estado local solo para la visibilidad de la contraseña
    var passwordVisible by remember { mutableStateOf(false) }

    // 3. REACCIÓN: Cuando el estado cambia a Success o Error
    LaunchedEffect(state) {
        when (state) {
            is AuthState.Success -> {
                Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                // Navegar a la lista (ajusta la ruta "listAll" si es necesario)
                navController.navigate("listAll") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Campo Usuario (Conectado al ViewModel) ---
        OutlinedTextField(
            value = viewModel.email, // Usamos la variable del ViewModel
            onValueChange = { viewModel.email = it },
            label = { Text("Email / Usuario") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is AuthState.Loading // Se bloquea si está cargando
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Campo Contraseña (Conectado al ViewModel) ---
        OutlinedTextField(
            value = viewModel.password, // Usamos la variable del ViewModel
            onValueChange = { viewModel.password = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar" else "Mostrar")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is AuthState.Loading // Se bloquea si está cargando
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Botón Login ---
        Button(
            onClick = {
                // 4. ACCIÓN: Llamamos al login real del servidor
                viewModel.login()
            },
            modifier = Modifier.fillMaxWidth(),
            // Deshabilitado si los campos están vacíos O si ya está cargando
            enabled = viewModel.email.isNotEmpty() && viewModel.password.isNotEmpty() && state !is AuthState.Loading
        ) {
            if (state is AuthState.Loading) {
                // Mostramos circulito si está cargando
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Entrar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón extra para ir al registro (según pide el PDF)
        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}