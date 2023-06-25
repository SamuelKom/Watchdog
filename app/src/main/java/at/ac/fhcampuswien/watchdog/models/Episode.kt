package at.ac.fhcampuswien.watchdog.models

import com.google.gson.annotations.SerializedName

data class Episode(

    @SerializedName("episode_number")
    val number: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("overview")
    val plot: String = "",

    @SerializedName("runtime")
    val length: Int = 0,

    @SerializedName("still_path")
    var poster: String = ""
)
