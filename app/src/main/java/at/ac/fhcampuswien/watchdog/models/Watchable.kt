package at.ac.fhcampuswien.watchdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(tableName = "watchable")
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
    var detailPoster: String = "",

    var isFavorite: Boolean = false,
    var isComplete: Boolean = false,
    var isPlanned: Boolean = false
)
