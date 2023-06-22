package at.ac.fhcampuswien.watchdog.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "series")
class Series(
    @SerializedName("name")
    val title: String = "",

    @SerializedName("first_air_date")
    val date: String = "",

    val episodes: Int = 0,
    val season: Int = 0,
    val genre: List<String> = listOf(),
    val director: String = "",
    val actors: String = ""

) : Watchable()