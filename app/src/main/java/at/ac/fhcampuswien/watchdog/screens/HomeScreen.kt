package at.ac.fhcampuswien.watchdog.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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

    val searchTextState by homeViewModel.searchTextState

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchBar(text = searchTextState,
                onTextChange = { homeViewModel.updateSearchTextState(newValue = it) },
                onCloseClicked = { homeViewModel.updateSearchTextState(newValue = "") },
                onSearchClicked = { Log.d("Searched Tet", it) })
        },
        drawerContent = {
            SideBar(
                modifier = Modifier,
                navController = navController,
                items = getSideScreens(),
                scaffoldState = scaffoldState
            )
        },
        drawerBackgroundColor = Color(0xFF19191A),
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) },
        backgroundColor = Color(0xFF19191A) //Color(R.color.grey)
    ) { padding ->
        println(padding)
        LazyColumn(
            Modifier.padding(start = 0.dp, top = 10.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item { HorizontalWatchableList(listTitle = "Popular Movies", homeViewModel.popularMovies) }
            item { HorizontalWatchableList(listTitle = "Top-Rated Movies", homeViewModel.topRatedMovies) }
            item { HorizontalWatchableList(listTitle = "Top-Rated Series", homeViewModel.topRatedSeries) }
            item { HorizontalWatchableList(listTitle = "Series airing today", homeViewModel.seriesAiringToday) }
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