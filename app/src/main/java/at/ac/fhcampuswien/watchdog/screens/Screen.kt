package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

    object Favorites : Screen(
        route = "favorites",
        title = "Favorites",
        icon = Icons.Default.Favorite
    )

    object Add : Screen(
        route = "add",
        title = "Add",
        icon = Icons.Default.Add
    )

    object Finished : Screen(
        route = "finished",
        title = "Finished",
        icon = Icons.Default.Done
    )

    object Planned : Screen(
        route = "planned",
        title = "Marked",
        icon = Icons.Default.List
    )
}