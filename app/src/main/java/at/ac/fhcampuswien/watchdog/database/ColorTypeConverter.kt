package at.ac.fhcampuswien.watchdog.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter

//@ProvidedTypeConverter
object ColorTypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromColor(color: Color): Int {
        return color.toArgb()
    }

    @TypeConverter
    @JvmStatic
    fun toColor(colorInt: Int): Color {
        return Color(colorInt)
    }
}
