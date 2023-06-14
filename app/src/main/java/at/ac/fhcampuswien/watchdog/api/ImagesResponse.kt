package at.ac.fhcampuswien.watchdog.api

import at.ac.fhcampuswien.watchdog.models.MovieImage
import com.google.gson.annotations.SerializedName

data class ImagesResponse(

    @SerializedName("backdrops")
    var results: List<MovieImage>
)