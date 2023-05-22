package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.models.getMovies
import at.ac.fhcampuswien.watchdog.utils.BotNavBar
import at.ac.fhcampuswien.watchdog.utils.OverViewList
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel

@Composable
fun FavoritesScreen(
    navController: NavController = rememberNavController(), homeViewModel: HomeViewModel
) {
    Scaffold(bottomBar = { BotNavBar(navController = navController) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyColumn {
                items(getMovies()){ movie ->
                    OverViewList(movie = movie)
                }
            }
        }
    }
}