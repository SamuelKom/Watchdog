package at.ac.fhcampuswien.watchdog.tmdb_api

import android.util.Log
import at.ac.fhcampuswien.watchdog.models.Genre
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
                else
                    homeViewModel.addTopRatedMovie(m)
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