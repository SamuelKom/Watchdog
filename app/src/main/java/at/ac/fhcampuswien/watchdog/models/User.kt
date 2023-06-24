package at.ac.fhcampuswien.watchdog.models

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import at.ac.fhcampuswien.watchdog.database.ColorTypeConverter
import java.util.*

@Entity(tableName = "user")
//@TypeConverters(ColorTypeConverter::class)
data class User (
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val name: String,

    var color: Int,//Color,

    val favGenres: String,//List<String>,

    val theme: String
)
