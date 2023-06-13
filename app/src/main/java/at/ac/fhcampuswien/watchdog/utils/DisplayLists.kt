package at.ac.fhcampuswien.watchdog.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.getMovies
import at.ac.fhcampuswien.watchdog.ui.theme.Shapes
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import coil.compose.AsyncImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyMovieGrid(homeViewModel: HomeViewModel, padding: PaddingValues) {
    val movieList = homeViewModel.movieList
    //val coroutineScope = rememberCoroutineScope()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            //coroutineScope.launch {
            items(movieList) { movie ->
                MovieImage(movie = movie)
                //println(movieList.count())
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
    //println(movie.poster)
}

@Composable
fun LibraryList( //call within a column
    movies: List<Movie> = getMovies(),
    //series: List<Series> = getSeries(),
    //other lambda functions
) {
    LazyColumn {
        items(movies){ movie ->
            ItemCard(
                movie = movie,
            )
        }
    }
}

@Composable
fun ItemCard(
    movie: Movie = getMovies()[0],
    //onClick: (String) -> Unit = {},
    //onFavClick: (Movie) -> Unit = {}
) {
    Card(modifier = Modifier
        .clickable {
            //onClick(movie.UID)
        }
        .fillMaxWidth()
        .padding(5.dp), shape = Shapes.large, elevation = 10.dp) {
        Column {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                MovieImage(movie = movie)
                //FavoriteIcon(movie, onFavClick)
            }

            //MovieDetails(modifier = Modifier.padding(12.dp), movie = movie)
        }
    }
}