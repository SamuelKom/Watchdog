package at.ac.fhcampuswien.watchdog.models

import androidx.compose.runtime.mutableStateListOf
import com.google.gson.annotations.SerializedName
import java.util.*

//@Entity(tableName = "series")
class Series(
    @SerializedName("name")
    val title: String = "",

    @SerializedName("first_air_date")
    val startDate: String = "",

    var endDate: String = "",

    var numberOfSeasons: Int = 0,

    var seasons: MutableList<Season> = mutableStateListOf(),

    ) : Watchable() {
    override fun getWatchableTitle(): String = title
    override fun getWatchableDate(): String {
        return startDate.split('-')[0] + "-" + endDate.split('-')[0]
    }
}