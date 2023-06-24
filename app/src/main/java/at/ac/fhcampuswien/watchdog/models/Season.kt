package at.ac.fhcampuswien.watchdog.models

import com.google.gson.annotations.SerializedName

data class Season(
    @SerializedName("episodes")
    val episodes: List<Episode> = emptyList(),

    @SerializedName("season_number")
    val number: Int = 0,

    var numberOfEpisodes : Int = episodes.size
)
