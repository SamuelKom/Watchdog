package at.ac.fhcampuswien.watchdog.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.database.UserDatabase
import at.ac.fhcampuswien.watchdog.database.UserRepository
import at.ac.fhcampuswien.watchdog.database.WatchableDatabase
import at.ac.fhcampuswien.watchdog.database.WatchableRepository
import at.ac.fhcampuswien.watchdog.screens.*
import at.ac.fhcampuswien.watchdog.viewmodels.*

@Composable
fun Navigation() {

    val db = WatchableDatabase.getDatabase(LocalContext.current)
    val repository = WatchableRepository(watchableDao = db.watchableDao())

    val userDB = UserDatabase.getDatabase(LocalContext.current)
    val userRepository = UserRepository(userDao = userDB.userDao())

    val homeFactory = HomeViewModelFactory(repository)
    val libraryFactory = LibraryViewModelFactory(repository)

    val profileFactory = ProfileViewModelFactory(userRepository)

    val homeViewModel: HomeViewModel = viewModel(factory = homeFactory) // home screen viewmodel
    val libraryViewModel: LibraryViewModel = viewModel(factory = libraryFactory) // home screen viewmodel

    val profileViewModel: ProfileViewModel = viewModel(factory = profileFactory)

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.ProfileSelection.route) {
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

        composable(route = Screen.ProfileSelection.route) {
            CheckLogin(navController = navController, profileViewModel = profileViewModel)
        }

        composable(route = Screen.AddProfile.route) {
            EditProfileScreen(navController = navController, profileViewModel = profileViewModel)
        }
    }
}