package at.ac.fhcampuswien.watchdog.api

import at.ac.fhcampuswien.watchdog.models.Movie
import com.google.gson.annotations.SerializedName

class MovieResponse(
    @SerializedName("results")
    var results: List<Movie>?,

) : TMDbResponse()