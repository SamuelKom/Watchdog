package at.ac.fhcampuswien.watchdog.api

import at.ac.fhcampuswien.watchdog.models.Series
import com.google.gson.annotations.SerializedName

class SeriesResponse(
    @SerializedName("results")
    var results: List<Series>?

) : TMDbResponse()