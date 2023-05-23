package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.api.fetchPopularMovies
import at.ac.fhcampuswien.watchdog.utils.BotNavBar
import at.ac.fhcampuswien.watchdog.utils.LazyMovieGrid
import at.ac.fhcampuswien.watchdog.utils.SideBar
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(), homeViewModel: HomeViewModel
){
    fetchPopularMovies(
        url = "https://api.themoviedb.org/3/movie/popular",
        homeViewModel = homeViewModel
    )
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(modifier = Modifier, navController = navController, items = getSideScreens()) },
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) }
    ) {padding ->
        LazyMovieGrid(homeViewModel = homeViewModel, padding = padding)
    }

}
