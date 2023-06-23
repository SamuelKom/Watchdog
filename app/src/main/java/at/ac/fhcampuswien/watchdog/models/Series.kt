package at.ac.fhcampuswien.watchdog.models

import com.google.gson.annotations.SerializedName
import java.util.*

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

) : Watchable() {
    override fun getWatchableTitle(): String = title
    override fun getWatchableDate(): String = date
}