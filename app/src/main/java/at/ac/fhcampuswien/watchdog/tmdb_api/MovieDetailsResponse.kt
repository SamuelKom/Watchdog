package at.ac.fhcampuswien.watchdog.tmdb_api

import at.ac.fhcampuswien.watchdog.models.Genre
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(

    @SerializedName("id")
    var TMDbID: Int = -1,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("overview")
    var plot: String = "", //

    @SerializedName("vote_average")
    var rating: Double = 0.0,

    @SerializedName("poster_path")
    var poster: String = "",

    @SerializedName("backdrop_path")
    var widePoster: String = "",

    @SerializedName("genres")
    val genres: List<Genre> = emptyList(),

    @SerializedName("runtime")
    val length: Int = 0,

    @SerializedName("release_date")
    val date: String = ""
)
