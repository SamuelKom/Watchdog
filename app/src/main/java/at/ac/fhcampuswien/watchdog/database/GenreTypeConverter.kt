package at.ac.fhcampuswien.watchdog.database

import androidx.room.TypeConverter

class GenreTypeConverter {
    // String to String
    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    // List<String> to String
    @TypeConverter
    fun toString(value: List<String>): String {
        return value.joinToString { "," }
    }
/*
    @TypeConverter
    fun genreListToString(value: List<String>): String =
        value.joinToString(separator = ",") { it.toString() }

    @TypeConverter
    fun stringToGenreList(value: String): List<Genre> = value.split(",").map { Genre.valueOf(it) }
 */
}