import androidx.room.TypeConverter
import at.ac.fhcampuswien.watchdog.models.Season
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DatabaseConverters {

    @TypeConverter
    fun fromString(value: String?): MutableList<String> {
        return value?.split(",")?.toMutableList() ?: mutableListOf()
    }

    @TypeConverter
    fun mutableListToString(value: MutableList<String>) = value.joinToString { "," }

    @TypeConverter
    fun listToString(value: List<String>) = value.joinToString { "," }

    @TypeConverter
    fun stringToList(value: String) = value.split(",").map { it.trim() }

    @TypeConverter
    fun seasonsToGson(seasons: MutableList<Season>): String {
        val gson = Gson()
        return gson.toJson(seasons)
    }

    @TypeConverter
    fun gsonToSeasons(value: String?): MutableList<Season> {
        val gson = Gson()
        val listType = object : TypeToken<MutableList<Season>>() {}.type
        return gson.fromJson(value, listType) ?: mutableListOf()
    }

    //@TypeConverter
    //fun genreListToString(value: List<Genre>): String =
    //    value.joinToString(separator = ",") { it.toString() }
//
    //@TypeConverter
    //fun stringToGenreList(value: String): List<Genre> = value.split(",").map { Genre.valueOf(it) }

}