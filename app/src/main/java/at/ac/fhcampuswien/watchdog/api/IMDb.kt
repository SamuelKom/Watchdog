package at.ac.fhcampuswien.watchdog.api

import android.util.Log
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org"
const val API_KEY = "0b29f9dc2baed571f1bdbfc35c34eeb3"

fun fetchPopularMovies(url: String, homeViewModel: HomeViewModel) {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(APIServices::class.java)

    CoroutineScope(Dispatchers.IO).launch {
        val response = service.getPopularMoviesReq(API_KEY)

        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val movieResponse = response.body();
                if (movieResponse != null) {
                    for (m in movieResponse.results!!) {
                        m.poster = "https://image.tmdb.org/t/p/original" + m.poster

                        homeViewModel.addMovie(m)
                    }
                }
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
    }
}