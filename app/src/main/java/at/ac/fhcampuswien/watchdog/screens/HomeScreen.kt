package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.api.fetchPopularMovies
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel()
){
    fetchPopularMovies(
        url = "https://api.themoviedb.org/3/movie/popular",
        homeViewModel = homeViewModel
    )

    LazyMovieGrid(homeViewModel = homeViewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyMovieGrid(homeViewModel: HomeViewModel) {
    val movieList = homeViewModel.movieList//.collectAsState();
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
        modifier = Modifier.fillMaxSize()
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