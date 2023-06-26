package at.ac.fhcampuswien.watchdog.tmdb_api

import at.ac.fhcampuswien.watchdog.models.Genre
import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)