package at.ac.fhcampuswien.watchdog.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface APIServices {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesReq(@Query("api_key") key: String): Response<MovieResponse>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMoviesReq(@Query("api_key") key: String): Response<MovieResponse>

    @GET("/3/movie/{id}/recommendations")
    suspend fun getMovieRecommendationsReq(@Path("id") id: Int, @Query("api_key") key: String): Response<MovieResponse>

    @GET("/3/movie/{id}/images")
    suspend fun getMoviePostersReq(@Path("id") id: Int, @Query("api_key") key: String): Response<ImagesResponse>

    @GET("/3/tv/top_rated")
    suspend fun getTopRatedSeriesReq(@Query("api_key") key: String): Response<SeriesResponse>

    @GET("/3/tv/airing_today")
    suspend fun getSeriesAiringTodayReq(@Query("api_key") key: String): Response<SeriesResponse>

    @GET("/3/tv/{id}/images")
    suspend fun getSeriesPostersReq(@Path("id") id: Int, @Query("api_key") key: String): Response<ImagesResponse>
}