package at.ac.fhcampuswien.watchdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
//@TypeConverters(ColorTypeConverter::class)
data class User(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),

    val name: String,

    val color: Int,//Color,

    val favGenres: String,//List<String>,

    val theme: String
)
