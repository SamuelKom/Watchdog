package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.R
import at.ac.fhcampuswien.watchdog.api.fetchPopularMovies
import at.ac.fhcampuswien.watchdog.api.fetchSeriesAiringToday
import at.ac.fhcampuswien.watchdog.api.fetchTopRatedMovies
import at.ac.fhcampuswien.watchdog.api.fetchTopRatedSeries
import at.ac.fhcampuswien.watchdog.utils.BotNavBar
import at.ac.fhcampuswien.watchdog.utils.HorizontalWatchableList
import at.ac.fhcampuswien.watchdog.utils.SearchBar
import at.ac.fhcampuswien.watchdog.utils.SideBar
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(), homeViewModel: HomeViewModel
) {
    fetchPopularMovies(homeViewModel = homeViewModel)
    fetchTopRatedMovies(homeViewModel = homeViewModel)
    fetchTopRatedSeries(homeViewModel = homeViewModel)
    fetchSeriesAiringToday(homeViewModel = homeViewModel)

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchBar(text = "Some text",
                onTextChange = {},
                onCloseClicked = {},
                onSearchClicked = {})
        },
        drawerContent = {
            SideBar(
                modifier = Modifier,
                navController = navController,
                items = getSideScreens(),
                scaffoldState = scaffoldState
            )
        },
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) },
        backgroundColor = MaterialTheme.colors.primary
    ) { padding ->
        println(padding)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item { Divider(Modifier.fillMaxWidth()) }
            item {
                HorizontalWatchableList(
                    listTitle = "Popular Movies",
                    watchableList = homeViewModel.popularMovies,
                    viewModel = homeViewModel
                )
            }
            item {
                HorizontalWatchableList(
                    listTitle = "Top-Rated Movies",
                    homeViewModel.topRatedMovies,
                    viewModel = homeViewModel
                )
            }
            item {
                HorizontalWatchableList(
                    listTitle = "Top-Rated Series",
                    homeViewModel.topRatedSeries,
                    viewModel = homeViewModel
                )
            }
            item {
                HorizontalWatchableList(
                    listTitle = "Series airing today",
                    homeViewModel.seriesAiringToday,
                    viewModel = homeViewModel
                )
            }
            item { Divider(Modifier.fillMaxWidth().height(60.dp)) }
        }
    }

    BackHandler {
        if (scaffoldState.drawerState.isOpen) {
            scope.launch { scaffoldState.drawerState.close() }
        } else {
            navController.popBackStack()
        }
    }
}