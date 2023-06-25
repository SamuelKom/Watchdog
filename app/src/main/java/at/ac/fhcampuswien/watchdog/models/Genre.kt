package at.ac.fhcampuswien.watchdog.models

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: Int = -1,

    @SerializedName("name")
    val name: String = ""
)
