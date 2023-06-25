package at.ac.fhcampuswien.watchdog.tmdb_api

import at.ac.fhcampuswien.watchdog.models.WatchableTrailer
import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("results")
    var results: List<WatchableTrailer>
)
