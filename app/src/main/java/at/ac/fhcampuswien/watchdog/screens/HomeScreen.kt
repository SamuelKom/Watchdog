package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    homeViewModel: ViewModel = viewModel()
){}