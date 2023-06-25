package at.ac.fhcampuswien.watchdog.database

import androidx.room.TypeConverter

class DatabaseConverters {

    @TypeConverter
    fun stringToList(value: String) = value.split(",").map { it.trim() }

    @TypeConverter
    fun mutableListToString(value: MutableList<String>) = value.joinToString { "," }

    @TypeConverter
    fun listToString(value: List<String>) = value.joinToString { "," }

    @TypeConverter
    fun stringToList(value: String) = value.split(",").map { it.trim() }

    //@TypeConverter
    //fun genreListToString(value: List<Genre>): String =
    //    value.joinToString(separator = ",") { it.toString() }
//
    //@TypeConverter
    //fun stringToGenreList(value: String): List<Genre> = value.split(",").map { Genre.valueOf(it) }

}