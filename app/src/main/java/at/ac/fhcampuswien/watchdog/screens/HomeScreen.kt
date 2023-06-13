package at.ac.fhcampuswien.watchdog.screens

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.api.fetchPopularMovies
import at.ac.fhcampuswien.watchdog.utils.BotNavBar
import at.ac.fhcampuswien.watchdog.utils.LazyMovieGrid
import at.ac.fhcampuswien.watchdog.utils.SearchBar
import at.ac.fhcampuswien.watchdog.utils.SideBar
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(), homeViewModel: HomeViewModel
){
    fetchPopularMovies(
        url = "https://api.themoviedb.org/3/movie/popular",
        homeViewModel = homeViewModel
    )

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchBar(text = "Some text",
                onTextChange = {},
                onCloseClicked = {},
                onSearchClicked ={})
        },
        drawerContent = { SideBar(modifier = Modifier, navController = navController, items = getSideScreens(), scaffoldState = scaffoldState) },
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) }
    ) {padding ->
        LazyMovieGrid(homeViewModel = homeViewModel, padding = padding)
    }

    BackHandler {
        if (scaffoldState.drawerState.isOpen) {
            scope.launch { scaffoldState.drawerState.close() }
        } else {
            navController.popBackStack()
        }
    }
}
