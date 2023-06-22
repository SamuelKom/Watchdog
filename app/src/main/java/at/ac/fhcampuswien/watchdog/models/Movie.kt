package at.ac.fhcampuswien.watchdog.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "movies")
class Movie(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("release_date")
    val date: String = ""

    // companies, actors, genres
) : Watchable()

/*
fun getMovies(): List<Movie> {
    return listOf(
        Movie(
            title = "Avatar",
            date = "2009",
        ),

        Movie(
            title = "300",
            date = "2006",
        ),

        Movie(
            title = "The Avengers",
            date = "2012",
        ),

        Movie(
            title = "The Wolf of Wall Street",
            date = "2013"
        ),

        Movie(
            title = "Interstellar",
            date = "2014",
           ),
        Movie(
            title = "Game of Thrones",
            date = "2011 - 2018",
            ),

        Movie(
            title = "Vikings",
            date = "2013–2020",
          ),

        Movie(
            title = "Breaking Bad",
            date = "2008–2013",
         ),

        Movie(
            title = "Narcos",
            date = "2015-",
          ),
        )
}*/