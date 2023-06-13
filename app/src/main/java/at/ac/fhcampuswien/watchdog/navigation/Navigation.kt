package at.ac.fhcampuswien.watchdog.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.database.MovieDatabase
import at.ac.fhcampuswien.watchdog.database.MovieRepository
import at.ac.fhcampuswien.watchdog.screens.*
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModelFactory
import at.ac.fhcampuswien.watchdog.viewmodels.LibraryViewModel
import at.ac.fhcampuswien.watchdog.viewmodels.LibraryViewModelFactory

@Composable
fun Navigation() {
    val db = MovieDatabase.getDatabase(LocalContext.current)
    val repository = MovieRepository(movieDao = db.movieDao())

    val homeFactory = HomeViewModelFactory(repository)
    val libraryFactory = LibraryViewModelFactory(repository)

    val homeViewModel: HomeViewModel = viewModel(factory = homeFactory) // home screen viewmodel
    val libraryViewModel: LibraryViewModel = viewModel(factory = libraryFactory) // home screen viewmodel

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, homeViewModel = homeViewModel)
        }
        composable(route = Screen.Library.route) {
            LibraryScreen(navController = navController, libraryViewModel = libraryViewModel)
        }
        composable(route = Screen.Account.route) {
            AccountScreen(navController = navController, homeViewModel = homeViewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController, homeViewModel = homeViewModel)
        }
    }
}