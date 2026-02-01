package com.example.myapplication

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.Nurse
import com.example.myapplication.NurseViewModel
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, viewModel: NurseViewModel) {

    val context = LocalContext.current
    val loggedId = viewModel.loggedInNurseId.value
    val currentUser = viewModel.nurses.value.firstOrNull { it.id == loggedId }
        ?: viewModel.nurses.value.firstOrNull()
        ?: Nurse(0, "", "", "", "", "", null)

    LaunchedEffect(loggedId) {
        if (loggedId != null) {
            viewModel.getNurseById(loggedId)
        }
    }

    var name by rememberSaveable(currentUser.id) { mutableStateOf(currentUser.name) }
    var surname by rememberSaveable(currentUser.id) { mutableStateOf(currentUser.surname) }
    var username by rememberSaveable(currentUser.id) { mutableStateOf(currentUser.username) }
    var email by rememberSaveable(currentUser.id) { mutableStateOf(currentUser.email) }

    LaunchedEffect(currentUser) {
        name = currentUser.name
        surname = currentUser.surname
        username = currentUser.username
        email = currentUser.email
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.nurse_icon)
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("listAll") {
                            popUpTo("listAll") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable {
                        Toast.makeText(context, "Función: Cambiar Foto (Próximamente)", Toast.LENGTH_SHORT).show()
                    }
            ) {
                val imageBitmap = remember(currentUser.photo) {
                    if (currentUser.photo != null) {
                        try {
                            val decodedString = Base64.decode(currentUser.photo, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size).asImageBitmap()
                        } catch (e: Exception) { null }
                    } else { null }
                }

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Foto Perfil",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Foto por defecto",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        tint = Color.White
                    )
                }

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar foto",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(34.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(6.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Información Personal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Apellidos") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (currentUser.id == 0L) {
                        Toast.makeText(context, "No hay usuario para actualizar", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val updated = Nurse(
                        id = currentUser.id,
                        name = name,
                        surname = surname,
                        username = username,
                        password = currentUser.password,
                        email = email,
                        photo = currentUser.photo
                    )
                    viewModel.updateNurse(currentUser.id, updated) { ok ->
                        if (ok) {
                            navController.navigate("listAll") {
                                popUpTo("listAll") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Cambios")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Darse de Baja")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar cuenta?") },
            text = { Text("Esta acción es irreversible. Se borrarán todos tus datos del sistema.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        if (currentUser.id == 0L) {
                            Toast.makeText(context, "No hay usuario para eliminar", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.deleteNurse(currentUser.id) { ok ->
                            if (ok) {
                                Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                                navController.navigate("register") {
                                    popUpTo(0)
                                }
                            } else {
                                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}