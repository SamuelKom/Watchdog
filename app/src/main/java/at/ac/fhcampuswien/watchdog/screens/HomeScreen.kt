package at.ac.fhcampuswien.watchdog.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchMoviesBySearchString
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchPopularMovies
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchSeriesAiringToday
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchTopRatedMovies
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchTopRatedSeries
import at.ac.fhcampuswien.watchdog.utils.BotNavBar
import at.ac.fhcampuswien.watchdog.utils.HorizontalWatchableList
import at.ac.fhcampuswien.watchdog.utils.LazyGrid
import at.ac.fhcampuswien.watchdog.utils.SearchBar
import at.ac.fhcampuswien.watchdog.utils.SideBar
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    homeViewModel: HomeViewModel,
    logout: () -> Unit
) {
    if (homeViewModel.popularMovies.isEmpty()) fetchPopularMovies(homeViewModel = homeViewModel)
    if (homeViewModel.topRatedMovies.isEmpty()) fetchTopRatedMovies(homeViewModel = homeViewModel)
    if (homeViewModel.topRatedSeries.isEmpty()) fetchTopRatedSeries(homeViewModel = homeViewModel)
    if (homeViewModel.seriesAiringToday.isEmpty()) fetchSeriesAiringToday(homeViewModel = homeViewModel)

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val searchedMovies by remember { mutableStateOf(mutableListOf<Movie>()) }
    val searchTextState by homeViewModel.searchTextState

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchBar(text = searchTextState,
                onTextChange = {
                    homeViewModel.updateSearchTextState(newValue = it)
                    fetchMoviesBySearchString(query = searchTextState, movies = searchedMovies)
                               },
                onCloseClicked = { homeViewModel.updateSearchTextState(newValue = "") },
                onSearchClicked = { Log.d("Searched Text", it) })
        },
        drawerContent = {
            SideBar(
                modifier = Modifier,
                navController = navController,
                items = getSideScreens(),
                scaffoldState = scaffoldState,
                logout = logout
            )
        },
        drawerBackgroundColor = Color(0xFF19191A),
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) },
        backgroundColor = Color(0xFF19191A) //Color(R.color.grey)
    ) { padding ->
        println(padding)
        if (searchTextState.isEmpty()) {
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
                item { Divider(
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)) }
            }
        } else {
            LazyGrid(list = searchedMovies, homeViewModel = homeViewModel)
        }
    }

    BackHandler {
        if (scaffoldState.drawerState.isOpen) {
            scope.launch { scaffoldState.drawerState.close() }
        } else {
            navController.popBackStack()
        }
    }
    SlideInPopup()
}

@Composable
fun SlideInPopup() {
    var popupVisible by remember { mutableStateOf(true) }

    val transition = updateTransition(targetState = popupVisible, label = "Popup Transition")
    val slideOffsetX by transition.animateFloat(
        label = "Slide Offset X",
        transitionSpec = {
            tween(durationMillis = 3000)
        }
    ) { isVisible ->
        if (isVisible) -120f else -1000f
    }

    LaunchedEffect(popupVisible) {
        if (popupVisible) {
            delay(8000) // Wait for 2 seconds
            popupVisible = false // Hide the popup after 2 seconds
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .fillMaxHeight(0.1f)
                .background(Color.Black.copy(alpha = 0.5f))
                .offset(x = slideOffsetX.dp)
                .align(Alignment.BottomStart)
        ) {

            // Content of the popup
            Text(
                text = "Popup Content",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}