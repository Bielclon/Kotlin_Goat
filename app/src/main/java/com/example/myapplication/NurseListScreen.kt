package com.example.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController, nurseService: NurseService = MockNurseService()) {

    var expanded by remember { mutableStateOf(false) }

    val allNurses = remember { nurseService.getAllNurses() }

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

                        // Opción 2: LOGIN
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
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = stringResource(R.string.nurse_message_icon),
                modifier = Modifier.size(48.dp),
                tint = colorResource(R.color.nurse_icon)
            )

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