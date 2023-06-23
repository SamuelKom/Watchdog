package at.ac.fhcampuswien.watchdog.utils

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.viewinterop.AndroidView
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
                                        viewModel.toggleFavourite(watchable = favouriteWatchable)
                                    }
                                },
                                onTogglePlannedClicked = { plannedWatchable ->
                                    coroutineScope.launch {
                                        viewModel.togglePlanned(watchable = plannedWatchable)
                                    }
                                },
                                onToggleWatchedClicked = { watchedWatchable ->
                                    coroutineScope.launch {
                                        viewModel.toggleWatched(watchable = watchedWatchable)
                                    }
                                })
                        }
                    }
                )
            })
    }
}

/*
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
}*/

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
            .clickable { popUpShown = !popUpShown }
    )
    if (popUpShown) {
        if (watchable is Movie) {
            MoviePopUp(
                movie = watchable,
                onToggleFavouriteClicked = onToggleFavouriteClicked,
                onTogglePlannedClicked = onTogglePlannedClicked,
                onToggleWatchedClicked = onToggleWatchedClicked,
                onDismissRequest = { popUpShown = !popUpShown }
            )
        } else if (watchable is Series) {
            SeriesPopUp(
                series = watchable,
                onToggleFavouriteClicked = onToggleFavouriteClicked,
                onTogglePlannedClicked = onTogglePlannedClicked,
                onToggleWatchedClicked = onToggleWatchedClicked,
                onDismissRequest = { popUpShown = !popUpShown }
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
    WatchablePopUp(
        watchable = movie,
        onDismissRequest = onDismissRequest,
        onToggleFavouriteClicked = onToggleFavouriteClicked,
        onTogglePlannedClicked = onTogglePlannedClicked,
        onToggleWatchedClicked = onToggleWatchedClicked,
        bottomContent = {
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
            Text(text = "Here comes further movie description", color = Color.White)
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
    WatchablePopUp(
        watchable = series,
        onDismissRequest = onDismissRequest,
        onToggleFavouriteClicked = onToggleFavouriteClicked,
        onTogglePlannedClicked = onTogglePlannedClicked,
        onToggleWatchedClicked = onToggleWatchedClicked,
        bottomContent = {
            Text(text = "Here comes further series description", color = Color.White)
        })
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
                        //.weight(weight = 1f, fill = false)
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
                                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 30.dp)
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
                                        onIconClicked = { onToggleFavouriteClicked(watchable) }
                                    )
                                    /** Plan to watch Icon */
                                    ClickableIcon(
                                        isActive = watchable.isPlanned,
                                        activeIcon = Icons.Default.Check,
                                        passiveIcon = Icons.Default.Add,
                                        iconColor = Color.White,
                                        onIconClicked = { onTogglePlannedClicked(watchable) }
                                    )
                                    /** Completed Icon */
                                    ClickableIcon(
                                        isActive = watchable.isComplete,
                                        activeIcon = R.drawable.watched,
                                        passiveIcon = R.drawable.not_watched,
                                        iconColor = Color.White,
                                        onIconClicked = { onToggleWatchedClicked(watchable) }
                                    )
                                }

                                // Date, Length
                                Text(
                                    text = "${
                                        watchable.getWatchableDate().split('-')[0]
                                    }   165 min",
                                    style = MaterialTheme.typography.body1,
                                    color = Color.White
                                )
                                //Divider(
                                //    modifier = Modifier.fillMaxWidth().height(12.dp)
                                //)
                            }
                            /** Space between left and right column */
                            Divider(
                                modifier = Modifier.weight(0.1f)
                            )

                            /** Right column */
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
fun PopUpBackgroundImage(posters: List<String>, imageIndex: Int) {

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
    isActive: Boolean,
    activeIcon: Any?,
    passiveIcon: Any?,
    iconColor: Color,
    onIconClicked: () -> Unit
) {
    var active by remember {
        mutableStateOf(isActive)
    }
    IconButton(
        onClick = {
            active = !active
            onIconClicked()
        },
        modifier = Modifier.size(28.dp)
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
                modifier = Modifier.size(28.dp)
            )
        } else if (activeIcon is ImageVector && passiveIcon is ImageVector) {
            Icon(
                imageVector =
                if (active) activeIcon
                else passiveIcon,
                contentDescription = "Close Icon",
                tint = iconColor,
                modifier = Modifier.size(28.dp)
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
            println("In here")
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
                onDismissRequest() },
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
            text = watchable.getWatchableTitle(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp)
                .padding(start = 6.dp, top = 0.dp, bottom = 0.dp, end = 0.dp),
            style = MaterialTheme.typography.h6,
            color = Color.White
        )
    }
}

/*
@Composable
fun LibraryList(
    //call within a column
    movies: List<Movie>,
    //series: List<Series> = getSeries(),
    //other lambda functions
) {
    LazyColumn {
        items(movies) { movie ->
            //ItemCard(
            //    movie = movie,
            //)
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
                //WatchableImage(watchable = movie)
                //FavoriteIcon(movie, onFavClick)
            }

            //MovieDetails(modifier = Modifier.padding(12.dp), movie = movie)
        }
    }
}
 */