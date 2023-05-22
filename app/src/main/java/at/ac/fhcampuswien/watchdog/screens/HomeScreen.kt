package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.getMovies
import at.ac.fhcampuswien.watchdog.navigation.BottomNavGraph
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
){
    /*fetchPopularMovies(
        url = "https://api.themoviedb.org/3/movie/popular",
        homeViewModel = homeViewModel
    )*/

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {padding ->
        LazyMovieGrid(homeViewModel = homeViewModel, padding = padding)
        BottomNavGraph(navController = navController, homeViewModel = homeViewModel)

    }



}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyMovieGrid(homeViewModel: HomeViewModel, padding: PaddingValues) {
    //val movieList = homeViewModel.movieList//.collectAsState();

    val movieList = getMovies()
    //val coroutineScope = rememberCoroutineScope()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            //coroutineScope.launch {
            items(movieList) { movie ->
                MovieImage(movie = movie)
                println(movieList.count())
            }
            //}
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    )
}

@Composable
fun MovieImage(movie: Movie){
    AsyncImage(
        model = movie.poster,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
    println(movie.poster)
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Add,
        Screen.Favorites,
        Screen.Home,
        Screen.Planned,
        Screen.Finished
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach {screen ->
            AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = { Text(text = screen.title) },
        icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") },
        selected = currentDestination?.hierarchy?.any{
            it.route == screen.route
        } == true,
        onClick = { navController.navigate(screen.route) }
    )
}