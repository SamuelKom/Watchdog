package at.ac.fhcampuswien.watchdog.tmdb_api

import android.util.Log
import at.ac.fhcampuswien.watchdog.models.Movie
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

private suspend fun fetchMovieDetailPoster(service: TMDbApi, movieID: Int): ImagesResponse? {

    return service.getMoviePostersReq(key = API_KEY, id = movieID).body()
}

private suspend fun fetchSeriesDetailPoster(service: TMDbApi, movieID: Int): ImagesResponse? {

    return service.getSeriesPostersReq(key = API_KEY, id = movieID).body()
}

fun fetchDetailPoster(watchable: Watchable) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val posters =
            if (watchable is Movie) fetchMovieDetailPoster(service, watchable.TMDbID)
            else fetchSeriesDetailPoster(service, watchable.TMDbID)

        if (posters != null) {
            val posterPaths = (mutableListOf <String>())
            for (poster in posters.results) {
                posterPaths.add(IMAGE_URL + poster.path)
            }
            watchable.detailPosters = posterPaths
        }
    }
}

fun fetchTrailer(watchable: Watchable) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        val trailers = service.getMovieTrailersReq(id = watchable.TMDbID, key = API_KEY)

        if (trailers.body() != null) {
            for (trailer in trailers.body()!!.results) {
                println("Name: " + trailer.name)
                if (trailer.site == "YouTube") {
                    watchable.trailer = YOUTUBE_EMBED_URL + trailer.id
                    return@launch
                }
            }
        }
    }
}

private fun createApiService(): TMDbApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(TMDbApi::class.java)
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
                m.detailPosters = listOf(IMAGE_URL + m.detailPosters)
                fetchDetailPoster(m)
                fetchTrailer(watchable = m)

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
                s.detailPosters = listOf(IMAGE_URL + s.detailPosters)
                fetchDetailPoster(s)
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