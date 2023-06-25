package at.ac.fhcampuswien.watchdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class LibraryItem (
    @PrimaryKey
    val TMDbID: Int = -1,
    var isFavorite: Boolean = false,
    var isComplete: Boolean = false,
    var isPlanned: Boolean = false
){}