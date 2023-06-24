package at.ac.fhcampuswien.watchdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

//@Entity(tableName = "movies")
class Movie(

    @SerializedName("title")
    val title: String = "",

    @SerializedName("release_date")
    val date: String = "",

    var length: Int = 0

    // companies, actors, genres
) : Watchable() {
     fun getWatchableTitle(): String = title
     fun getWatchableDate(): String = date.split('-')[0]
}