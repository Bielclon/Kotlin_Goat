package com.example.myapplication

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController, viewModel: NurseViewModel) {

    var expanded by remember { mutableStateOf(false) }

    val allNurses = viewModel.nurses.value

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.nurse_list))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.nurse_icon),
                    titleContentColor = colorResource(R.color.white),
                    actionIconContentColor = colorResource(R.color.white)
                ),
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.nurse_message_icon)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Inicio") },
                            onClick = {
                                expanded = false
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            leadingIcon = { Icon(Icons.Default.Home, stringResource(id = R.string.nurse_message_icon)) }
                        )

                        DropdownMenuItem(
                            text = { Text("Login") },
                            onClick = {
                                expanded = false
                                navController.navigate("login")
                            },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, stringResource(R.string.nurse_message_icon)) }
                        )

                        DropdownMenuItem(
                            text = { Text("Buscar") },
                            onClick = {
                                navController.navigate("search")
                            },
                            leadingIcon = { Icon(Icons.Default.Search, stringResource(R.string.nurse_message_icon)) }
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
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(allNurses) { nurse ->
                    NurseItem(nurse = nurse)
                }
            }
        }
    }
}


@Composable
fun NurseItem(nurse: Nurse) {
    var isSelected by remember { mutableStateOf(false) }

    val imageBitmap = remember(nurse.photo) {
        if (nurse.photo != null) {
            try {
                val decodedString = Base64.decode(nurse.photo, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size).asImageBitmap()
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                elevation = 10.dp,
                shape = MaterialTheme.shapes.medium,
                clip = false,
                ambientColor = colorResource(id = R.color.transparent),
                spotColor = colorResource(id = R.color.black).copy(alpha = 0.5f)
            )
            .clickable {
                isSelected = !isSelected
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                colorResource(id = R.color.nurse_card_bg_selected)
            } else {
                colorResource(id = R.color.nurse_card_bg_normal)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        )

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.nurse_message_icon),
                    modifier = Modifier.size(48.dp),
                    tint = colorResource(R.color.nurse_icon)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${nurse.name} ${nurse.surname}",
                    style = MaterialTheme.typography.titleMedium
                )
                Row() {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = stringResource(R.string.nurse_message_icon),
                        modifier = Modifier.size(16.dp),
                        tint = colorResource(R.color.nurse_icon_small)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = nurse.username,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_primary)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(R.string.nurse_message_icon),
                        modifier = Modifier.size(16.dp),
                        tint = colorResource(R.color.nurse_icon_small)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = nurse.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.text_primary)
                    )
                }
            }
        }
    }
}