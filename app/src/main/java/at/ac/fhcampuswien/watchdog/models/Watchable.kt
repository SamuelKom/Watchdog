package at.ac.fhcampuswien.watchdog.models

import androidx.compose.runtime.*
import com.google.gson.annotations.SerializedName
import java.util.UUID

//@Entity(tableName = "watchable")
open class Watchable(
    //@PrimaryKey
    var UID: String = UUID.randomUUID().toString(), //

    @SerializedName("id")
    var TMDbID: Int = -1, //

    @SerializedName("overview")
    var plot: String = "", //

    @SerializedName("vote_average") //
    var rating: Double = 0.0, //

    @SerializedName("poster_path")
    var poster: String = "",

    @SerializedName("backdrop_path")
    var widePoster: String = "",

    var detailPosters: List<String> = mutableStateListOf(),

    var genres: MutableList<String> = mutableStateListOf(),

    var trailer: String = "",

    var isFavorite : MutableState<Boolean> = mutableStateOf(false),
    var isWatched : MutableState<Boolean> = mutableStateOf(false),
    var isPlanned : MutableState<Boolean> = mutableStateOf(false),
    var isLiked : MutableState<Boolean> = mutableStateOf(false),
    var isDisliked : MutableState<Boolean> = mutableStateOf(false),

    var hasAllDetails: Boolean = false
    ) {


     open fun getWatchableTitle() : String = ""
     open fun getWatchableDate() : String = ""
}
