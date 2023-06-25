package at.ac.fhcampuswien.watchdog.tmdb_api

import at.ac.fhcampuswien.watchdog.models.Genre
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("genres")
    val genres: List<Genre> = emptyList(),

    @SerializedName("runtime")
    val length: Int = 0
)
