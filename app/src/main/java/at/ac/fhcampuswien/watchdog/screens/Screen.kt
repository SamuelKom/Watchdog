package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen (
     val route: String,
     val title: String,
     val icon: ImageVector
) {
    object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    /*object DetailScreen : Screen("detail/{movieId}") {
        fun withId(id: String): String {
            return this.route.replace(oldValue = "{movieId}", newValue = id)
        }
    }
    object FavoriteScreen : Screen("favorite")

    object AddMovieScreen : Screen("addMovie")*/
}