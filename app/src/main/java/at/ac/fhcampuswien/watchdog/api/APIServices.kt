package at.ac.fhcampuswien.watchdog.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesReq(@Query("api_key") api_key: String): Response<MovieResponse>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMoviesReq(@Query("api_key") api_key: String): Response<MovieResponse>

    @GET("/3/movie/385687/images")
    suspend fun getMoviePostersReq(@Query("api_key") api_key: String): Response<ImagesResponse>

    @GET("/3/tv/top_rated")
    suspend fun getTopRatedSeriesReq(@Query("api_key") api_key: String): Response<SeriesResponse>

    @GET("/3/tv/airing_today")
    suspend fun getSeriesAiringTodayReq(@Query("api_key") api_key: String): Response<SeriesResponse>
}