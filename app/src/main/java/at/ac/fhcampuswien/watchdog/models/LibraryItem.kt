package at.ac.fhcampuswien.watchdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class LibraryItem (
    @PrimaryKey
    val TMDbID: Int = -1,
    var isMovie: Boolean = false,
    var poster: String = "",
    var isFavorite: Boolean = false,
    var isWatched: Boolean = false,
    var isPlanned: Boolean = false
){}