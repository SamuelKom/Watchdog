package at.ac.fhcampuswien.watchdog.utils

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Series
import at.ac.fhcampuswien.watchdog.models.Watchable
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import coil.compose.AsyncImage
import at.ac.fhcampuswien.watchdog.R
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import android.os.Handler
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import at.ac.fhcampuswien.watchdog.models.Episode
import at.ac.fhcampuswien.watchdog.models.Season
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchDetails
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchSimilarMovies
import at.ac.fhcampuswien.watchdog.ui.theme.Shapes
import kotlin.random.Random


@Composable
fun HorizontalWatchableList(
    listTitle: String,
    watchableList: List<Watchable>,
    viewModel: HomeViewModel
) {

    val coroutineScope = rememberCoroutineScope()

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
                            WatchableImage(
                                watchable = watchable,
                                onToggleFavouriteClicked = { favouriteWatchable ->
                                    coroutineScope.launch {
                                        viewModel.updateFavorite(watchable = favouriteWatchable)
                                    }
                                },
                                onTogglePlannedClicked = { plannedWatchable ->
                                    coroutineScope.launch {
                                        viewModel.updatePlanned(watchable = plannedWatchable)
                                    }
                                },
                                onToggleWatchedClicked = { watchedWatchable ->
                                    coroutineScope.launch {
                                        viewModel.updateComplete(watchable = watchedWatchable)
                                    }
                                })
                        }
                    }
                )
            })
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyMovieGrid(movieList: List<Watchable>) {
    //val movieList = homeViewModel.popularMovies //.collectAsState();

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(movieList) { watchable ->
                WatchableImage(
                    watchable = watchable,
                    onToggleFavouriteClicked = { favouriteWatchable ->
                        /*coroutineScope.launch {
                            viewModel.updateFavorite(watchable = favouriteWatchable)
                        }*/
                    },
                    onTogglePlannedClicked = { plannedWatchable ->
                        /*coroutineScope.launch {
                            viewModel.updatePlanned(watchable = plannedWatchable)
                        }*/
                    },
                    onToggleWatchedClicked = { watchedWatchable ->
                        /*coroutineScope.launch {
                            viewModel.updateComplete(watchable = watchedWatchable)
                        }*/
                    })
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )
}

@Composable
fun WatchableImage(
    watchable: Watchable,
    onToggleFavouriteClicked: (Watchable) -> Unit,
    onTogglePlannedClicked: (Watchable) -> Unit,
    onToggleWatchedClicked: (Watchable) -> Unit
) {
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
            .clip(RoundedCornerShape(12.dp))
            .clickable { popUpShown = true }
    )
    if (popUpShown) {
        if (watchable is Movie) {
            MoviePopUp(
                movie = watchable,
                onToggleFavouriteClicked = onToggleFavouriteClicked,
                onTogglePlannedClicked = onTogglePlannedClicked,
                onToggleWatchedClicked = onToggleWatchedClicked,
                onDismissRequest = { popUpShown = false }
            )
        } else if (watchable is Series) {
            SeriesPopUp(
                series = watchable,
                onToggleFavouriteClicked = onToggleFavouriteClicked,
                onTogglePlannedClicked = onTogglePlannedClicked,
                onToggleWatchedClicked = onToggleWatchedClicked,
                onDismissRequest = { popUpShown = false }
            )
        }
    }
}

@Composable
fun MoviePopUp(
    movie: Movie,
    onDismissRequest: () -> Unit,
    onToggleFavouriteClicked: (Watchable) -> Unit,
    onTogglePlannedClicked: (Watchable) -> Unit,
    onToggleWatchedClicked: (Watchable) -> Unit
) {
    if (!movie.hasAllDetails)
        fetchDetails(movie)

    val similarMovies = remember { mutableStateListOf<Movie>() }

    if (similarMovies.isEmpty())
        fetchSimilarMovies(movieID = movie.TMDbID, movies = similarMovies)

    WatchablePopUp(
        watchable = movie,
        onDismissRequest = onDismissRequest,
        onToggleFavouriteClicked = onToggleFavouriteClicked,
        onTogglePlannedClicked = onTogglePlannedClicked,
        onToggleWatchedClicked = onToggleWatchedClicked,
        bottomContent = {
            PopUpMoviesBottomContainer(similarMovies)
        })
}

@Composable
fun SeriesPopUp(
    series: Series,
    onDismissRequest: () -> Unit,
    onToggleFavouriteClicked: (Watchable) -> Unit,
    onTogglePlannedClicked: (Watchable) -> Unit,
    onToggleWatchedClicked: (Watchable) -> Unit
) {
    if (!series.hasAllDetails)
        fetchDetails(series)

    WatchablePopUp(
        watchable = series,
        onDismissRequest = onDismissRequest,
        onToggleFavouriteClicked = onToggleFavouriteClicked,
        onTogglePlannedClicked = onTogglePlannedClicked,
        onToggleWatchedClicked = onToggleWatchedClicked,
        bottomContent = {
            PopUpSeriesBottomContainer(seasons = series.seasons)
        })
}

@Composable
fun PopUpMoviesBottomContainer(movies: MutableList<Movie>) {

    Text(
        text = if (movies.isEmpty()) "No similar movies available" else "Similar",
        fontSize = MaterialTheme.typography.h6.fontSize,
        color = Color.White,
        modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
    )
    if (movies.isEmpty()) return

    /** Container for all movies */
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)

    ) {
        /** For every 2 items, add a new Row */
        for (i in 0..movies.size step 2) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                /** Movie */
                for (j in i..i + 1) {
                    if (j >= movies.size) return
                    Column(
                        modifier = Modifier
                            .width(178.dp)
                            .height(300.dp)
                            .background(Color(0xFF222222))
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            AsyncImage(
                                model =
                                if(movies[j].widePoster.endsWith("original")) movies[j].poster
                                else movies[j].widePoster,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            )
                            /** Button to add the movie to plan to watch */
                            var planToWatch by remember {
                                mutableStateOf(movies[j].isPlanned)
                            }
                            ClickableIcon(
                                onIconClicked = { planToWatch = !planToWatch },
                                isActive = planToWatch,
                                activeIcon = Icons.Default.Check,
                                passiveIcon = Icons.Default.Add,
                                iconColor = Color.White,
                                horizontalAnimation = false,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .background(Color(0xCC222222))
                                    .clip(RoundedCornerShape(5.dp))
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            /** Title */
                            Text(
                                text = movies[j].title,
                                color = Color.White,
                                fontSize = MaterialTheme.typography.body1.fontSize,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            /** Plot */
                            Text(
                                text = movies[j].plot,
                                color = Color.LightGray,
                                fontSize = MaterialTheme.typography.body2.fontSize,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PopUpSeriesBottomContainer(seasons: MutableList<Season>) {

    var expanded by remember { mutableStateOf(false) }
    var seasonIndex by remember {
        mutableStateOf(
            if (seasons.isEmpty()) 0
            else Random.nextInt(seasons.size)
        )
    }

    /** First Container */
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        if (seasons.isEmpty()) {
            Text(
                text = "No Episodes available",
                fontSize = MaterialTheme.typography.h6.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            return
        }
        Text(text = "Episodes", fontSize = MaterialTheme.typography.h6.fontSize, color = Color.White)
        Text(
            text = "Season ${seasons[seasonIndex].number}",
            fontSize = MaterialTheme.typography.h6.fontSize,
            color = Color.White
        )
        /*
        Box(modifier = Modifier.fillMaxWidth(0.5f)) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = "Season ${seasons[seasonIndex].number}",
                    onValueChange = {},
                    readOnly = true,
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    for (season in seasons) {
                        println("Creating drop down item")
                        DropdownMenuItem(
                            onClick = {
                                seasonIndex = season.number - 1
                                expanded = false
                            }
                        ) {
                            Text(
                                text = "Season ${season.number} | (${season.numberOfEpisodes})",
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }*/
    }

    /** For all episodes */
    PopUpSeriesEpisodes(episodes = seasons[seasonIndex].episodes)
}

@Composable
fun PopUpSeriesEpisodes(episodes: List<Episode>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        for (episode in episodes) {
            Divider(
                color = Color.DarkGray,
                thickness = 1.dp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = episode.poster,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .wrapContentHeight()
                )
                Column {
                    Text(text = "${episode.number}: ${episode.name}", color = Color.White)
                    Text(
                        text = episode.plot,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun WatchablePopUp(
    watchable: Watchable,
    bottomContent: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    onToggleFavouriteClicked: (Watchable) -> Unit,
    onTogglePlannedClicked: (Watchable) -> Unit,
    onToggleWatchedClicked: (Watchable) -> Unit
) {
    //println("Watchable ID: " + watchable.TMDbID)
    var showBackground by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(true) }
    var isExpanded by remember { mutableStateOf(false) }

    /** To animate the popup background (opacity outside and black inside) */
    val innerBackgroundColor by animateColorAsState(
        targetValue = if (showBackground) Color(0f, 0f, 0f, 1f) else Color(0f, 0f, 0f, 0f),
        animationSpec = tween(100),
    )
    val outerBackgroundColor by animateColorAsState(
        targetValue = if (isExpanded) Color(0f, 0f, 0f, 0.5f) else Color(0f, 0f, 0f, 0f),
        animationSpec = tween(600),
    )

    /** To animate the popup size and position */
    val popupScale by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0.3f,
        animationSpec = tween(durationMillis = 400)
    )
    val translateY by animateDpAsState(
        targetValue = if (isExpanded) 0.dp else 550.dp,
        animationSpec = tween(durationMillis = 650),
        finishedListener = {
            if (it == 0.dp) {
                showBackground = true
            } else if (it > 0.dp) {
                onDismissRequest()
            }
        }
    )

    /** Trigger the animations 1 frame after they've been drawn
    so they're assured to been scaled correctly */
    LaunchedEffect(showPopup) {
        if (showPopup) {
            withFrameNanos { }
            isExpanded = true
        } else {
            isExpanded = false
        }
    }

    /** Screen-size popup with gray background */
    Popup {
        Card(
            backgroundColor = outerBackgroundColor,
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxSize()
        ) {
            /** Popup with movie detail content */
            Popup(alignment = Alignment.Center) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .fillMaxHeight(0.9f)
                        .background(innerBackgroundColor)
                        .graphicsLayer {
                            scaleX = popupScale
                            scaleY = popupScale
                            translationY = translateY.toPx()
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.Black)
                            .verticalScroll(rememberScrollState())
                    ) {
                        /** Top container with background image, close button and title */
                        PopUpTopBox(
                            watchable = watchable
                        ) {
                            showPopup = !showPopup
                            showBackground = !showBackground
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 15.dp)
                        ) {
                            /** Left column */
                            Column(
                                modifier = Modifier.weight(0.65f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                /** Row for tagging icons */
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {

                                    /** Favourite Icon */
                                    ClickableIcon(
                                        isActive = watchable.isFavorite,
                                        activeIcon = Icons.Default.Favorite,
                                        passiveIcon = Icons.Default.FavoriteBorder,
                                        iconColor = Color(0xFFC71E1E),
                                        horizontalAnimation = true,
                                        onIconClicked = { onToggleFavouriteClicked(watchable) }
                                    )
                                    /** Plan to watch Icon */
                                    ClickableIcon(
                                        isActive = watchable.isPlanned,
                                        activeIcon = Icons.Default.Check,
                                        passiveIcon = Icons.Default.Add,
                                        iconColor = Color.White,
                                        horizontalAnimation = false,
                                        onIconClicked = { onTogglePlannedClicked(watchable) }
                                    )
                                    /** Completed Icon */
                                    ClickableIcon(
                                        isActive = watchable.isComplete,
                                        activeIcon = R.drawable.watched,
                                        passiveIcon = R.drawable.not_watched,
                                        iconColor = Color.White,
                                        horizontalAnimation = true,
                                        onIconClicked = { onToggleWatchedClicked(watchable) }
                                    )
                                }
                                PopUpLeftWatchableInformation(genres = watchable.genres)

                            }
                            /** Space between left and right column */
                            Divider(
                                modifier = Modifier.weight(0.1f)
                            )

                            /** Right column */
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(0.32f)
                            ) {
                                PopUpRightWatchableInformation(
                                    date = "Date", //watchable.getWatchableDate(),
                                    rating = watchable.rating,
                                    length =
                                    if (watchable is Movie) "${watchable.length}"
                                    else ""
                                )
                            }
                        }
                        /** Plot */
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

@Composable
fun PopUpRightWatchableInformation(date: String, length: String, rating: Double) {

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        /** Date */
        Text(
            text = date,
            style = MaterialTheme.typography.body1,
            color = Color.LightGray
        )
        /** If movie, length */
        if (length != "0" && length.isNotEmpty()) {
            Text(
                text = "$length min",
                style = MaterialTheme.typography.body1,
                color = Color.LightGray
            )
        }
    }
    /** Rating */
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Rating:", color = Color.Gray)
        Divider(modifier = Modifier.width(8.dp))
        Text(text = "$rating", color = Color.LightGray)
    }
}

@Composable
fun PopUpLeftWatchableInformation(genres: List<String>) {

    /** Genres */
    if (genres.isEmpty()) return
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {

        var text = ""
        for (genre in genres) {
            text += "$genre, "
        }
        text = text.removeRange(text.lastIndex - 1, text.lastIndex)
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = Color.LightGray
        )
    }
}

@Composable
fun PopUpBackgroundImage(posters: List<String>, imageIndex: Int) {

    if (posters.isEmpty()) return
    Crossfade(
        animationSpec = tween(3000),
        targetState = imageIndex,
    ) { idx ->
        AsyncImage(
            model = posters[idx],
            contentScale = ContentScale.Crop,
            contentDescription = "Background poster",
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
    }
}

@Composable
fun YoutubeScreen(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->

            val webView = WebView(context)
            webView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    // Call JavaScript function to simulate a click on the video element
                    webView.evaluateJavascript("document.getElementById('player').click();", null)
                }
            }

            val settings: WebSettings = webView.settings
            settings.javaScriptEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false

            val iframeHTML = """
                    <html>
                    <body style="margin:0px;padding:0px;">
                    <iframe id="player" width="100%" height="100%" src="${videoUrl}?autoplay=1" frameborder="0" allowfullscreen></iframe>
                    </body>
                    </html>
                """.trimIndent()

            webView.loadData(iframeHTML, "text/html", "utf-8")
            webView
        }
    )
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeIcon: Any?,
    passiveIcon: Any?,
    iconColor: Color,
    horizontalAnimation: Boolean = true,
    onIconClicked: () -> Unit
) {
    var active by remember {
        mutableStateOf(isActive)
    }
    val transition = updateTransition(targetState = active, label = "Icon Transition")
    val iconRotation by transition.animateFloat(
        label = "",
        transitionSpec = {
            tween(durationMillis = 300)
        }
    ) { activity ->
        if (activity) 0f else 180f
    }

    IconButton(
        onClick = {
            active = !active
            onIconClicked()
        },
        modifier = modifier.size(28.dp)
    ) {
        if (activeIcon is Int) {
            Icon(
                painter = rememberAsyncImagePainter(
                    model =
                    if (active) activeIcon
                    else passiveIcon
                ),
                contentDescription = "Icon",
                tint = iconColor,
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer(
                        rotationY =
                        if (horizontalAnimation) iconRotation
                        else 0f,
                        rotationZ = if (!horizontalAnimation) iconRotation
                        else 0f,
                    )
            )
        } else if (activeIcon is ImageVector && passiveIcon is ImageVector) {
            Icon(
                imageVector =
                if (active) activeIcon
                else passiveIcon,
                contentDescription = "Close Icon",
                tint = iconColor,
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer(
                        rotationY =
                        if (horizontalAnimation) iconRotation
                        else 0f,
                        rotationZ = if (!horizontalAnimation) iconRotation
                        else 0f,
                    )
            )
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun PopUpTopBox(
    watchable: Watchable,
    onDismissRequest: () -> Unit
) {
    val mod = Modifier
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

    var visible by remember {
        mutableStateOf(true)
    }

    var imageIndex by remember { mutableStateOf(0) }

    /** To update the watchable background image */
    val mainHandler = Handler(Looper.getMainLooper())
    val updateImage = object : Runnable {
        override fun run() {
            if (watchable.detailPosters.isNotEmpty())
                imageIndex = Random.nextInt(watchable.detailPosters.size)
            mainHandler.postDelayed(this, 7000)
        }
    }

    if (visible) {
        visible = false
        if (!mainHandler.hasCallbacks(updateImage))
            mainHandler.postDelayed(updateImage, 7000)
    }

    /** Image with title */
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {

        PopUpBackgroundImage(posters = watchable.detailPosters, imageIndex = imageIndex)

        //if (watchable.trailer.isNotEmpty())
        //    YoutubeScreen(videoUrl = watchable.trailer, modifier = mod)

        /** Close button */
        IconButton(
            onClick = {
                visible = false
                onDismissRequest()
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Icon",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        /** Title */
        Text(
            text = "Title", // watchable.getWatchableTitle(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp)
                .padding(start = 6.dp, top = 0.dp, bottom = 0.dp, end = 0.dp),
            style = MaterialTheme.typography.h5,
            color = Color.White
        )
    }
}


@Composable
fun LibraryList(
    modifier: Modifier,
    watchables: List<Watchable>,
    listTitle: String
    //series: List<Series> = getSeries(),
    //other lambda functions
) {
    Row{
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = listTitle,
            style = MaterialTheme.typography.h6,
            color = Color.White
        )
    }
    LazyMovieGrid(movieList = watchables)
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
                //WatchableImage(watchable = movie)
                //FavoriteIcon(movie, onFavClick)
            }

            //MovieDetails(modifier = Modifier.padding(12.dp), movie = movie)
        }
    }
}
