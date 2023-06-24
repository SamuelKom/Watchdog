package at.ac.fhcampuswien.watchdog.tmdb_api

import at.ac.fhcampuswien.watchdog.models.Genre
import com.google.gson.annotations.SerializedName

data class SeriesDetailsResponse(
    @SerializedName("genres")
    val genres: List<Genre> = emptyList(),

    @SerializedName("last_air_date")
    val endDate: String = "",

    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int = 0
)