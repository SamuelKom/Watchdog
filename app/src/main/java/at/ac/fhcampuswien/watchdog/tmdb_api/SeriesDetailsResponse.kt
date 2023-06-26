package at.ac.fhcampuswien.watchdog.tmdb_api

import at.ac.fhcampuswien.watchdog.models.Genre
import com.google.gson.annotations.SerializedName

data class SeriesDetailsResponse(

    @SerializedName("id")
    var TMDbID: Int = -1,

    @SerializedName("name")
    val title: String = "",

    @SerializedName("overview")
    var plot: String = "", //

    @SerializedName("vote_average")
    var rating: Double = 0.0,

    @SerializedName("poster_path")
    var poster: String = "",

    @SerializedName("backdrop_path")
    var widePoster: String = "",

    @SerializedName("first_air_date")
    val startDate: String = "",

    @SerializedName("genres")
    val genres: List<Genre> = emptyList(),

    @SerializedName("last_air_date")
    val endDate: String = "",

    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int = 0
)