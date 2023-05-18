package at.ac.fhcampuswien.watchdog.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIServices {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesReq(@Query("api_key") api_key: String): Response<TMDbResponse>
}