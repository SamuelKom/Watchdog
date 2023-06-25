package at.ac.fhcampuswien.watchdog.youtube_api

import android.util.Log
import at.ac.fhcampuswien.watchdog.models.Watchable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
const val API_KEY = "AIzaSyARHFgTlAanOTA9lMl91H6YVXFNw6ORQGs"
const val SHA1_FINGERPRINT = "60:37:60:EB:3E:9B:60:0E:41:A6:17:BB:15:E7:17:AC:85:E7:6B:E9"

fun fetchVideo(watchable: Watchable, videoID: String) {
    val service = createApiService()
    CoroutineScope(Dispatchers.IO).launch {
        println("CURRENT ID: " + videoID)
        setTrailerURLFromResponse(
            response = service.getVideoReq(id = videoID, key = API_KEY),
            watchable = watchable
        )
    }
}

private fun setTrailerURLFromResponse(
    response: Response<YouTubeVideo>,
    watchable: Watchable
) {
    println("RESPONSE")
    println(response.message().toString())
    println(response.code().toString())
    if (response.isSuccessful) {
        val video = response.body()
        if (video != null) {
            println("Video: " + video.id)
            watchable.trailer = video.id
            }
    } else {
        Log.e("YOUTUBE_ERROR", response.errorBody().toString())
    }
}

private fun createApiService(): YouTubeApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(YouTubeApi::class.java)
}