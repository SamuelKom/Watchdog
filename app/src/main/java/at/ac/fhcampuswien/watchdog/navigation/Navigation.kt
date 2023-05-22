package at.ac.fhcampuswien.watchdog.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.screens.HomeScreen
import at.ac.fhcampuswien.watchdog.screens.Screen
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel

@Composable
fun Navigation() {

    //val homeFactory = HomeScreenViewModelFactory(repository)

    val homeViewModel: HomeViewModel = viewModel() // home screen viewmodel
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route){
            HomeScreen(navController = navController, homeViewModel = homeViewModel)
        }
        composable(route = Screen.Favorites.route){
            HomeScreen(navController = navController, homeViewModel = homeViewModel)
        }

        /* build a route like: root/detail-screen/id=34
        composable(
            Screen.DetailScreen.route,
            arguments = listOf(navArgument(name = DETAIL_ARGUMENT_KEY) {type = NavType.StringType})
        ) { backStackEntry ->    // backstack contains all information from navhost
            DetailScreen(navController = navController,
                moviesViewModel = detailScreenViewModel,
                movieId = backStackEntry.arguments?.getString(DETAIL_ARGUMENT_KEY))   // get the argument from navhost that will be passed
        }*/
    }
}