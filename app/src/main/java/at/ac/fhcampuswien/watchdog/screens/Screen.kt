package at.ac.fhcampuswien.watchdog.screens


sealed class Screen (val route: String) {
    object HomeScreen : Screen("home")

    /*object DetailScreen : Screen("detail/{movieId}") {
        fun withId(id: String): String {
            return this.route.replace(oldValue = "{movieId}", newValue = id)
        }
    }
    object FavoriteScreen : Screen("favorite")

    object AddMovieScreen : Screen("addMovie")*/
}