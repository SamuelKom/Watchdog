package at.ac.fhcampuswien.watchdog.models

import com.google.gson.annotations.SerializedName

data class WatchableTrailer (
    @SerializedName("key")
    val id: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("site")
    val site: String = "",

    @SerializedName("official")
    val isOfficial: Boolean = false,
)