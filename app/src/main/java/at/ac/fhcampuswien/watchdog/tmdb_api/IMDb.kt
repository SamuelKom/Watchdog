package at.ac.fhcampuswien.watchdog.tmdb_api

import android.util.Log
import at.ac.fhcampuswien.watchdog.models.Genre
import at.ac.fhcampuswien.watchdog.models.LibraryItem
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Season
import at.ac.fhcampuswien.watchdog.models.Series
import at.ac.fhcampuswien.watchdog.models.Watchable
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import at.ac.fhcampuswien.watchdog.youtube_api.fetchVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "0b29f9dc2baed571f1bdbfc35c34eeb3"
const val BASE_URL = "https://api.themoviedb.org"
const val IMAGE_URL = "https://image.tmdb.org/t/p/original"
const val YOUTUBE_EMBED_URL = "https://www.youtube.com/embed/"

fun fetchPopularMovies(homeViewModel: HomeViewModel) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        createMoviesFromResponse(
            response = service.getPopularMoviesReq(API_KEY),
            homeViewModel = homeViewModel,
            type = 1
        )
    }
}

fun fetchTopRatedMovies(homeViewModel: HomeViewModel) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        createMoviesFromResponse(
            response = service.getTopRatedMoviesReq(API_KEY),
            homeViewModel = homeViewModel,
            type = 2
        )
    }
}

fun fetchSimilarMovies(movieID: Int, movies: MutableList<Movie>) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val moviesResponse = service.getSimilarMoviesReq(id = movieID, key = API_KEY)
        if (moviesResponse.body() != null) {
            val similarMovies = moviesResponse.body()!!.results
            for (movie in similarMovies!!) {
                movie.poster = IMAGE_URL + movie.poster
                movie.widePoster = IMAGE_URL + movie.widePoster
                movies.add(movie)
            }
        }
    }
}

fun fetchTopRatedSeries(homeViewModel: HomeViewModel) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        createSeriesFromResponse(
            response = service.getTopRatedSeriesReq(API_KEY),
            homeViewModel = homeViewModel,
            type = 1
        )
    }
}

fun fetchSeriesAiringToday(homeViewModel: HomeViewModel) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        createSeriesFromResponse(
            response = service.getSeriesAiringTodayReq(API_KEY),
            homeViewModel = homeViewModel,
            type = 2
        )
    }
}

fun fetchGenresByWatchableType(type: String = "movie", genreNames: MutableList<String> = mutableListOf(), genreNumbers: MutableList<Int> = mutableListOf()) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val genresResponse = service.getGenresByWatchableType(watchableType = type, key = API_KEY)

        if (genresResponse.body() != null) {
            for (genre in genresResponse.body()!!.genres) {
                println("Genre: " + genre.name)
                genreNames.add(genre.name)
                genreNumbers.add(genre.id)
            }
        }
    }
}

fun fetchMoviesByGenres(homeViewModel: HomeViewModel, genres: List<Int>) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        println("genres" + genres.joinToString())
        createMoviesFromResponse(
            response = service.getMoviesByGenres(API_KEY, genres.joinToString()),
            homeViewModel = homeViewModel,
            type = 3
        )
    }
}

fun fetchMoviesBySearchString(query: String, movies: MutableList<Movie>) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val moviesResponse = service.getMoviesBySearchStringReq(query = query, key = API_KEY)

        if (moviesResponse.body() != null) {
            movies.clear()
            for (movie in moviesResponse.body()!!.results!!) {
                movie.poster = IMAGE_URL + movie.poster
                movie.widePoster = IMAGE_URL + movie.widePoster
                movie.detailPosters = listOf(movie.widePoster)
                movies.add(movie)
            }
        }
    }
}

fun fetchWatchablesByLibraryItems(libraryItems: List<LibraryItem>, watchables: MutableList<Watchable>) {

    for (item in libraryItems) {
        if (item.isMovie) {
            val movie = Movie()
            movie.TMDbID = item.TMDbID
            fetchDetails(watchable = movie)
            watchables.add(movie)
        } else {
            val series = Series()
            series.TMDbID = item.TMDbID
            fetchDetails(watchable = series)
            watchables.add(series)
        }
    }
}

private fun fetchDetailPoster(watchable: Watchable) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val posters =
            if (watchable is Movie) service.getMoviePostersReq(key = API_KEY, id = watchable.TMDbID).body()
            else service.getSeriesPostersReq(key = API_KEY, id = watchable.TMDbID).body()

        if (posters != null) {
            val posterPaths = (mutableListOf <String>())
            for (poster in posters.results) {
                posterPaths.add(IMAGE_URL + poster.path)
            }
            watchable.detailPosters = posterPaths
        }
    }
}

private fun fetchTrailer(watchable: Watchable) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val trailers =
            if (watchable is Movie) service.getMovieTrailersReq(id = watchable.TMDbID, key = API_KEY)
            else service.getSeriesTrailersReq(id = watchable.TMDbID, key = API_KEY)

        if (trailers.body() != null) {
            for (trailer in trailers.body()!!.results) {
                if (trailer.site == "YouTube" && trailer.type == "Trailer" || trailer.type == "Teaser") {
                    watchable.trailer = YOUTUBE_EMBED_URL + trailer.id
                    return@launch
                }
            }
        }
    }
}

private fun fetchSeriesDetails(series: Series) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val detailsResponse = service.getSeriesDetails(id = series.TMDbID, key = API_KEY)

        if (detailsResponse.body() != null) {
            val details = detailsResponse.body()
            val genres = mutableListOf<String>()
            for (genre in details!!.genres) {
                genres.add(genre.name)
            }
            series.genres = genres
            series.endDate = details.endDate
            series.numberOfSeasons = details.numberOfSeasons

            // If normally default values are unassigned, set them
            if (series.title == "") series.title = details.title
            if (series.startDate == "") series.startDate = details.startDate
            if (series.plot == "") series.plot = details.plot
            if (series.widePoster == "") series.widePoster = IMAGE_URL + details.widePoster
            if (series.poster == "") series.poster = IMAGE_URL + details.poster
            if (series.rating == 0.0) series.rating = details.rating

            // Season details
            for (i in 1..series.numberOfSeasons) {
                val seasonResponse = service.getSeasonDetails(id = series.TMDbID, number = i, key = API_KEY)

                if (seasonResponse.body() != null) {
                    val season = seasonResponse.body()
                    season!!.numberOfEpisodes = season.episodes.size
                    for (episode in season.episodes) {
                        episode.poster = IMAGE_URL + episode.poster
                    }
                    series.seasons.add(season)
                }
            }
        }
    }
}

private fun fetchMovieDetails(movie: Movie) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val detailsResponse = service.getMovieDetails(id = movie.TMDbID, key = API_KEY)

        if (detailsResponse.body() != null) {
            val details = detailsResponse.body()
            for (genre in details!!.genres) {
                movie.genres.add(genre.name)
            }
            movie.length = details.length

            // If normally default values are unassigned, set them
            if (movie.title == "") movie.title = details.title
            if (movie.date == "") movie.date = details.date
            if (movie.plot == "") movie.plot = details.plot
            if (movie.widePoster == "") movie.widePoster = IMAGE_URL + details.widePoster
            if (movie.poster == "") movie.poster = IMAGE_URL + details.poster
            if (movie.rating == 0.0) movie.rating = details.rating

        }
    }
}

fun fetchDetails(watchable: Watchable) {

    if (watchable is Movie) fetchMovieDetails(watchable)
    else if (watchable is Series) fetchSeriesDetails(watchable)

    fetchDetailPoster(watchable)
    fetchTrailer(watchable)

    watchable.hasAllDetails = true
}

private fun createMoviesFromResponse(
    response: Response<MovieResponse>,
    homeViewModel: HomeViewModel,
    type: Int
) {
    if (response.isSuccessful) {
        val movieResponse = response.body()
        if (movieResponse != null) {
            for (m in movieResponse.results!!) {
                m.poster = IMAGE_URL + m.poster
                m.widePoster = IMAGE_URL + m.widePoster
                m.detailPosters = listOf(m.widePoster)

                if (type == 1)
                    homeViewModel.addPopularMovie(m)
                else if (type == 2)
                    homeViewModel.addTopRatedMovie(m)
                else if (type == 3)
                    homeViewModel.addRecommendedMovie(m)
            }
        }
    } else {
        Log.e("RETROFIT_ERROR", response.code().toString())
    }
}

private fun createSeriesFromResponse(
    response: Response<SeriesResponse>,
    homeViewModel: HomeViewModel,
    type: Int
) {
    if (response.isSuccessful) {
        val seriesResponse = response.body()
        if (seriesResponse != null) {
            for (s in seriesResponse.results!!) {
                s.poster = IMAGE_URL + s.poster
                s.widePoster = IMAGE_URL + s.widePoster
                s.detailPosters = listOf(s.widePoster)

                if (type == 1)
                    homeViewModel.addTopRatedSeries(s)
                else if (type == 2)
                    homeViewModel.addSeriesAiringToday(s)
            }
        }
    } else {
        Log.e("RETROFIT_ERROR", response.code().toString())
    }
}

private fun createApiService(): TMDbApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(TMDbApi::class.java)
}