package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.models.getMovies
import at.ac.fhcampuswien.watchdog.utils.BotNavBar
import at.ac.fhcampuswien.watchdog.utils.OverViewList
import at.ac.fhcampuswien.watchdog.utils.SideBar
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel

@Composable
fun LibraryScreen(
    navController: NavController = rememberNavController(), homeViewModel: HomeViewModel
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(modifier = Modifier, navController = navController, items = getSideScreens()) },
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState, items = getBottomScreens()) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(text = "Library")
            LazyColumn {
                items(getMovies()){ movie ->
                    OverViewList(movie = movie)
                }
            }
        }
    }
}