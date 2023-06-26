package at.ac.fhcampuswien.watchdog.navigation

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.database.LibraryDatabase
import at.ac.fhcampuswien.watchdog.database.UserDatabase
import at.ac.fhcampuswien.watchdog.database.UserRepository
import at.ac.fhcampuswien.watchdog.database.WatchableRepository
import at.ac.fhcampuswien.watchdog.screens.*
import at.ac.fhcampuswien.watchdog.viewmodels.*

@Composable
fun Navigation() {
    var loggedIn by remember { mutableStateOf(false) }
    val sharedPrefs =
        LocalContext.current.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    val login: (String) -> Unit = {user ->
        loggedIn = true
        sharedPrefs.edit().putString("user", user).apply()
    }

    val logout: () -> Unit  = {
        loggedIn = false
        sharedPrefs.edit().remove("user").apply()
    }

    if (loggedIn) {
        val user = sharedPrefs.getString("user", null)
        println(user)

        val context: Context = LocalContext.current

        val db = remember { LibraryDatabase.getDatabase(context, user!!) }
        val repository = WatchableRepository(libraryDao = db.watchableDao())

        val userDB = UserDatabase.getDatabase(LocalContext.current)
        val userRepository = UserRepository(userDao = userDB.userDao())

        val homeFactory = HomeViewModelFactory(repository)
        val libraryFactory = LibraryViewModelFactory(repository)

        val profileFactory = ProfileViewModelFactory(userRepository)

        val homeViewModel: HomeViewModel = viewModel(factory = homeFactory) // home screen viewmodel
        val libraryViewModel: LibraryViewModel =
            viewModel(factory = libraryFactory) // home screen viewmodel

        println(libraryViewModel.favoriteWatchables.toString())

        val profileViewModel: ProfileViewModel = viewModel(factory = profileFactory)

        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(route = Screen.Home.route) {
                HomeScreen(navController = navController, homeViewModel = homeViewModel, logout = logout)
            }
            composable(route = Screen.Library.route) {
                LibraryScreen(navController = navController, libraryViewModel = libraryViewModel, logout = logout)
            }
            composable(route = Screen.Account.route) {
                AccountScreen(navController = navController, profileViewModel = profileViewModel, logout = logout, userId = user!!)
            }
            composable(route = Screen.Settings.route) {
                SettingsScreen(navController = navController, homeViewModel = homeViewModel, logout = logout)
            }
        }
    } else {
        val userDB = UserDatabase.getDatabase(LocalContext.current)
        val userRepository = UserRepository(userDao = userDB.userDao())

        val profileFactory = ProfileViewModelFactory(userRepository)

        val profileViewModel: ProfileViewModel = viewModel(factory = profileFactory)

        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Screen.ProfileSelection.route) {
            composable(route = Screen.ProfileSelection.route) {
                ProfileSelectionScreen(navController = navController,
                    profileViewModel = profileViewModel,
                    login = login)
            }

            composable(route = Screen.AddProfile.route) {
                EditProfileScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    login = login
                )
            }
        }
    }
}