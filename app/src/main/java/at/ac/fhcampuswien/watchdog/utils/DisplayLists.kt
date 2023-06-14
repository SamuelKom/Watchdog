package at.ac.fhcampuswien.watchdog.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Series
import at.ac.fhcampuswien.watchdog.models.Watchable
import at.ac.fhcampuswien.watchdog.ui.theme.Shapes
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import coil.compose.AsyncImage

@Composable
fun HorizontalWatchableList(listTitle: String, watchableList: List<Watchable>) {

    Card(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = Modifier
            .height(250.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = listTitle,
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    content = {
                        items(watchableList) { watchable ->
                            WatchableImage(watchable = watchable)
                        }
                    }
                )
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyMovieGrid(homeViewModel: HomeViewModel, padding: PaddingValues) {
    val movieList = homeViewModel.popularMovies //.collectAsState();

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(movieList) { movie ->
                WatchableImage(watchable = movie)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    )
}

@Composable
fun WatchableImage(watchable: Watchable) {
    var popUpShown by remember {
        mutableStateOf(false)
    }
    AsyncImage(
        model = watchable.poster,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                popUpShown = !popUpShown

            }
    )
    if (popUpShown) {
        if (watchable is Movie) {
            MoviePopUp(
                movie = watchable,
                onDismissRequest = { popUpShown = !popUpShown }
            )
        } else if (watchable is Series) {
            SeriesPopUp(
                series = watchable,
                onDismissRequest = { popUpShown = !popUpShown }
            )
        }
    }
}

@Composable
fun MoviePopUp(movie: Movie, onDismissRequest: () -> Unit) {

    WatchablePopUp(
        title = movie.title,
        watchable = movie,
        onDismissRequest = onDismissRequest,
        bottomContent = {
            Text(text = "Here comes further movie description", color = Color.White)
        })
}

@Composable
fun SeriesPopUp(series: Series, onDismissRequest: () -> Unit) {

    WatchablePopUp(
        title = series.title,
        watchable = series,
        onDismissRequest = onDismissRequest,
        bottomContent = {
            Text(text = "Here comes further series description", color = Color.White)
        })
}

@Composable
fun WatchablePopUp(
    title: String,
    watchable: Watchable,
    bottomContent: @Composable () -> Unit,
    onDismissRequest: () -> Unit
) {

    // For Gray background
    Popup {
        Card(
            backgroundColor = Color(0f, 0f, 0f, 0.5f),
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Pop up with movie detail content
            Popup(
                alignment = Alignment.BottomCenter,
                offset = IntOffset(0, -50),
                onDismissRequest = { onDismissRequest() },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .fillMaxHeight(0.9f)
                        .background(Color.Black)
                ) {

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                    ) {
                        // Image with e.g. title
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            AsyncImage(
                                model = watchable.detailPoster,
                                contentScale = ContentScale.Crop,
                                contentDescription = "$title poster",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .drawWithCache {
                                        val gradient = Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black),
                                            startY = size.height / 3,
                                            endY = size.height
                                        )
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(gradient, blendMode = BlendMode.Multiply)
                                        }
                                    }
                            )
                            Text(
                                text = title,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(6.dp)
                                    .padding(start = 6.dp, top = 0.dp, bottom = 10.dp, end = 0.dp),
                                style = MaterialTheme.typography.h6,
                                color = Color.White
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
                            //.wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(0.65f),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Date, Length
                                Text(
                                    text = "Date",// "${watchable.date.split('-')[0]}   165 min",
                                    style = MaterialTheme.typography.body1,
                                    color = Color.White
                                )
                                //Divider(
                                //    modifier = Modifier.fillMaxWidth().height(12.dp)
                                //)
                            }
                            Divider(
                                modifier = Modifier.weight(0.1f)
                            )

                            Column(
                                modifier = Modifier.weight(0.25f)
                            ) {
                                // Cast, Genres, "Diese Serie ist schräg"
                                Text(
                                    text = "Cast: Johnny Depp, Angelina Jolie",
                                    style = MaterialTheme.typography.body2,
                                    color = Color.White
                                )
                                Text(
                                    text = "Rating: ${watchable.rating}",
                                    style = MaterialTheme.typography.body2,
                                    color = Color.White
                                )
                            }
                        }
                        // Plot
                        Text(
                            text = watchable.plot,
                            style = MaterialTheme.typography.body2,
                            lineHeight = 20.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp)
                        )
                        bottomContent()
                    }
                }
            }
        }
    }
}

/*
@Composable
fun WatchablePopUp(watchable: Watchable, onDismissRequest: () -> Unit ) {

    // For Gray background
    Popup {
        Card(
            backgroundColor = Color(0f, 0f, 0f, 0.5f),
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Pop up with movie detail content
            Popup(
                alignment = Alignment.BottomCenter,
                offset = IntOffset(0, -50),
                onDismissRequest = { onDismissRequest() },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .fillMaxHeight(0.9f)
                        .background(Color.Black)
                ) {

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                    ) {
                        // Image with e.g. title
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            AsyncImage(
                                model = watchable.detailPoster,
                                contentScale = ContentScale.Crop,
                                contentDescription = "Title",//"${watchable.title} poster",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .drawWithCache {
                                        val gradient = Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black),
                                            startY = size.height / 3,
                                            endY = size.height
                                        )
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(gradient, blendMode = BlendMode.Multiply)
                                        }
                                    }
                            )
                            Text(
                                text = watchable.titleName,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(6.dp)
                                    .padding(start = 6.dp, top = 0.dp, bottom = 10.dp, end = 0.dp),
                                style = MaterialTheme.typography.h6,
                                color = Color.White
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
                            //.wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(0.65f),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Date, Length
                                Text(
                                    text = "Date",// "${watchable.date.split('-')[0]}   165 min",
                                    style = MaterialTheme.typography.body1,
                                    color = Color.White
                                )
                                //Divider(
                                //    modifier = Modifier.fillMaxWidth().height(12.dp)
                                //)
                            }
                            Divider(
                                modifier = Modifier.weight(0.1f)
                            )

                            Column(
                                modifier = Modifier.weight(0.25f)
                            ) {
                                // Cast, Genres, "Diese Serie ist schräg"
                                Text(
                                    text = "Cast: Johnny Depp, Angelina Jolie",
                                    style = MaterialTheme.typography.body1,
                                    color = Color.White
                                )
                                Text(
                                    text = "Rating: ${watchable.rating}",
                                    style = MaterialTheme.typography.body1,
                                    color = Color.White
                                )
                            }
                        }
                        // Plot
                        Text(
                            text = watchable.plot,
                            style = MaterialTheme.typography.body2,
                            lineHeight = 20.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        }
    }
}
 */


@Composable
fun LibraryList(
    //call within a column
    movies: List<Movie>,
    //series: List<Series> = getSeries(),
    //other lambda functions
) {
    LazyColumn {
        items(movies) { movie ->
            ItemCard(
                movie = movie,
            )
        }
    }
}

@Composable
fun ItemCard(
    movie: Movie,
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
                WatchableImage(watchable = movie)
                //FavoriteIcon(movie, onFavClick)
            }

            //MovieDetails(modifier = Modifier.padding(12.dp), movie = movie)
        }
    }
}