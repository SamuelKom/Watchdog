package at.ac.fhcampuswien.watchdog.models

import androidx.compose.runtime.mutableStateListOf
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity
open class Watchable(
    @PrimaryKey
    val UID: String = UUID.randomUUID().toString(),

    @SerializedName("id")
    val TMDbID: Int = -1,

    @SerializedName("overview")
    val plot: String = "",

    @SerializedName("vote_average")
    val rating: Double = 0.0,

    @SerializedName("poster_path")
    var poster: String = "",

    @SerializedName("backdrop_path")
    var widePoster: String = "",

    var detailPosters: List<String> = mutableStateListOf(),

    var genres: MutableList<String> = mutableStateListOf(),

    var trailer: String = "",

    var isFavorite: Boolean = false,
    var isComplete: Boolean = false,
    var isPlanned: Boolean = false,

    var hasAllDetails: Boolean = false
    ) {
     //fun getWatchableTitle() : String
     //fun getWatchableDate() : String
}
