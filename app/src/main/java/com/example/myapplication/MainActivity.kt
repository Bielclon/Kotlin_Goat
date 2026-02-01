package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.NurseSearch
import com.example.myapplication.ProfileScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val nurseViewModel = viewModel<NurseViewModel>()

            NavHost(navController = navController, startDestination = "login") {

                composable("register") {
                    RegisterScreen(navController = navController)
                }

                composable("home") {
                    HomeScreen(navController)
                }

                composable("listAll") {
                    ListScreen(navController, nurseViewModel)
                }

                composable("login") {
                    LoginScreen(navController)
                }

                composable("search") {
                    NurseSearch(navController)
                }

                composable("profile") {
                    val vm = viewModel<NurseViewModel>()
                    ProfileScreen(navController = navController, viewModel = vm)
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}