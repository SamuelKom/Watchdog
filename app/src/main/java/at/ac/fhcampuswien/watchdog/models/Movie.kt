package at.ac.fhcampuswien.watchdog.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Movie(
    val UID: String = UUID.randomUUID().toString(),

    @SerializedName("id")
    val TMDb_id: Int = -1,

    @SerializedName("original_title")
    val title: String = "",

    @SerializedName("release_date")
    val date: String = "",

    @SerializedName("overview")
    val plot: String = "",

    @SerializedName("poster_path")
    var poster: String = "",

    @SerializedName("vote_average")
    val rating: Double = 0.0,

    var isFavorite: Boolean = false

    // companies, actors, genres
)