package at.ac.fhcampuswien.watchdog.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.screens.FavoritesScreen
import at.ac.fhcampuswien.watchdog.screens.HomeScreen
import at.ac.fhcampuswien.watchdog.screens.Screen
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel

@Composable
fun Navigation() {

    //val homeFactory = HomeScreenViewModelFactory(repository)

    val homeViewModel: HomeViewModel = viewModel() // home screen viewmodel
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, homeViewModel = homeViewModel)
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(navController = navController, homeViewModel = homeViewModel)
        }
    }
}