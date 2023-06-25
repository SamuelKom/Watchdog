package at.ac.fhcampuswien.watchdog.tmdb_api

import at.ac.fhcampuswien.watchdog.models.Season
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface TMDbApi {

    /**                          ***       Movies      ***                          **/
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesReq(@Query("api_key") key: String): Response<MovieResponse>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMoviesReq(@Query("api_key") key: String): Response<MovieResponse>

    @GET("/3/movie/{id}/similar")
    suspend fun getSimilarMoviesReq(@Path("id") id: Int, @Query("api_key") key: String): Response<MovieResponse>

    @GET("/3/movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int, @Query("api_key") key: String): Response<MovieDetailsResponse>

    @GET("/3/movie/{id}/videos")
    suspend fun getMovieTrailersReq(@Path("id") id: Int, @Query("api_key") key: String): Response<VideosResponse>

    @GET("/3/movie/{id}/images")
    suspend fun getMoviePostersReq(@Path("id") id: Int, @Query("api_key") key: String): Response<ImagesResponse>

    @GET("/3/movie/{id}/recommendations")
    suspend fun getMovieRecommendationsReq(@Path("id") id: Int, @Query("api_key") key: String): Response<MovieResponse>


    /**                          ***     Series      ***                          **/
    @GET("/3/tv/top_rated")
    suspend fun getTopRatedSeriesReq(@Query("api_key") key: String): Response<SeriesResponse>

    @GET("/3/tv/airing_today")
    suspend fun getSeriesAiringTodayReq(@Query("api_key") key: String): Response<SeriesResponse>

    @GET("/3/tv/{id}")
    suspend fun getSeriesDetails(@Path("id") id: Int, @Query("api_key") key: String): Response<SeriesDetailsResponse>

    @GET("/3/tv/{id}/season/{number}")
    suspend fun getSeasonDetails(@Path("id") id: Int, @Path("number") number: Int, @Query("api_key") key: String): Response<Season>

    @GET("/3/tv/{id}/videos")
    suspend fun getSeriesTrailersReq(@Path("id") id: Int, @Query("api_key") key: String): Response<VideosResponse>

    @GET("/3/tv/{id}/images")
    suspend fun getSeriesPostersReq(@Path("id") id: Int, @Query("api_key") key: String): Response<ImagesResponse>
}