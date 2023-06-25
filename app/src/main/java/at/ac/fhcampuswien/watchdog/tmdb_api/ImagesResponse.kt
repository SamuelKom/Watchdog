package at.ac.fhcampuswien.watchdog.tmdb_api

import at.ac.fhcampuswien.watchdog.models.WatchableImage
import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("backdrops")
    var results: List<WatchableImage>
)