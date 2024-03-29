package at.ac.fhcampuswien.watchdog.models

import com.google.gson.annotations.SerializedName
import java.util.*

//@Entity(tableName = "movies")
class Movie(

    @SerializedName("title")
    var title: String = "",

    @SerializedName("release_date")
    var date: String = "",

    var length: Int = 0

    // companies, actors, genres
) : Watchable() {
    override fun getWatchableTitle(): String = title
    override fun getWatchableDate(): String = date.split('-')[0]
}