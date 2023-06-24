package at.ac.fhcampuswien.watchdog.youtube_api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {
    @GET("videos")
    suspend fun getVideoReq(@Query("part") part: String = "snippet,status", @Query("id") id: String, @Query("key") key: String): Response<YouTubeVideo>
}