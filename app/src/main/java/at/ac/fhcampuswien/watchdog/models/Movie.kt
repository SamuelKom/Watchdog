package at.ac.fhcampuswien.watchdog.models

import java.util.*

data class Movie(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val year: String = "",
    val genre: List<String> = listOf(),
    val director: String = "",
    val actors: String = "",
    val plot: String = "",
    val images: List<String> = listOf(),
    val rating: Double = 0.0,
    var isFavorite: Boolean = false
)