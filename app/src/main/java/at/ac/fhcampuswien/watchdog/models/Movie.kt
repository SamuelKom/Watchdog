package at.ac.fhcampuswien.watchdog.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Movie(
    val UID: String = UUID.randomUUID().toString(),

    @SerializedName("id")
    val TMDb_id: Int = -1,

    @SerializedName("original_title")
    val title: String = "",

    @SerializedName("release_date")
    val date: String = "",

    @SerializedName("overview")
    val plot: String = "",

    @SerializedName("poster_path")
    var poster: String = "",

    @SerializedName("vote_average")
    val rating: Double = 0.0,

    var isFavorite: Boolean = false

    // companies, actors, genres
)

fun getMovies(): List<Movie> {
    return listOf(
        Movie(UID = "tt0499549",
            title = "Avatar",
            date = "2009",
            plot = "A paraplegic marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjEyOTYyMzUxNl5BMl5BanBnXkFtZTcwNTg0MTUzNA@@._V1_SX1500_CR0,0,1500,999_AL_.jpg",
            rating = 7.9
        ),

        Movie(UID = "tt0416449",
            title = "300",
            date = "2006",
            plot = "King Leonidas of Sparta and a force of 300 men fight the Persians at Thermopylae in 480 B.C.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTMwNTg5MzMwMV5BMl5BanBnXkFtZTcwMzA2NTIyMw@@._V1_SX1777_CR0,0,1777,937_AL_.jpg",
            rating = 7.7
        ),

        Movie(UID = "tt0848228",
            title = "The Avengers",
            date = "2012",
            plot = "Earth's mightiest heroes must come together and learn to fight as a team if they are to stop the mischievous Loki and his alien army from enslaving humanity.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTA0NjY0NzE4OTReQTJeQWpwZ15BbWU3MDczODg2Nzc@._V1_SX1777_CR0,0,1777,999_AL_.jpg",
            rating = 8.1),

        Movie(UID = "tt0993846",
            title = "The Wolf of Wall Street",
            date = "2013",
            plot = "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BNDIwMDIxNzk3Ml5BMl5BanBnXkFtZTgwMTg0MzQ4MDE@._V1_SX1500_CR0,0,1500,999_AL_.jpg",
            rating = 8.2),

        Movie(UID = "tt0816692",
            title = "Interstellar",
            date = "2014",
            plot = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjA3NTEwOTMxMV5BMl5BanBnXkFtZTgwMjMyODgxMzE@._V1_SX1500_CR0,0,1500,999_AL_.jpg",
            rating = 8.6),
        Movie(UID = "tt0944947",
            title = "Game of Thrones",
            date = "2011 - 2018",
            plot = "While a civil war brews between several noble families in Westeros, the children of the former rulers of the land attempt to rise up to power. Meanwhile a forgotten race, bent on destruction, plans to return after thousands of years in the North.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BNDc1MGUyNzItNWRkOC00MjM1LWJjNjMtZTZlYWIxMGRmYzVlXkEyXkFqcGdeQXVyMzU3MDEyNjk@._V1_SX1777_CR0,0,1777,999_AL_.jpg",
            rating = 9.5),


        Movie(UID = "tt2306299",
            title = "Vikings",
            date = "2013–2020",
            plot = "The world of the Vikings is brought to life through the journey of Ragnar Lothbrok, the first Viking to emerge from Norse legend and onto the pages of history - a man on the edge of myth.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjM5MTM1ODUxNV5BMl5BanBnXkFtZTgwNTAzOTI2ODE@._V1_.jpg",
            rating = 9.5),

        Movie(UID = "tt0903747",
            title = "Breaking Bad",
            date = "2008–2013",
            plot = "A high school chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine in order to secure his family's financial future.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTgyMzI5NDc5Nl5BMl5BanBnXkFtZTgwMjM0MTI2MDE@._V1_SY1000_CR0,0,1498,1000_AL_.jpg",
            rating = 9.5),

        Movie(UID = "tt2707408",
            title = "Narcos",
            date = "2015-",
            plot = "A chronicled look at the criminal exploits of Colombian drug lord Pablo Escobar.",
            poster = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTk2MDMzMTc0MF5BMl5BanBnXkFtZTgwMTAyMzA1OTE@._V1_SX1500_CR0,0,1500,999_AL_.jpg",
            rating = 9.5),

        )
}