package at.ac.fhcampuswien.watchdog.api

import com.google.gson.annotations.SerializedName

open class TMDbResponse (
    @SerializedName("page")
    var page: Int? = 0,

    @SerializedName("total_pages")
    var total_pages: Int? = 0,

    @SerializedName("total_results")
    var total_results: Int? = 0
)