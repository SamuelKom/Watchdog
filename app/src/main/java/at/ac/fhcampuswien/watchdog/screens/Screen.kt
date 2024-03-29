package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen (
     val route: String,
     val title: String,
     val icon: ImageVector
) {
    object ProfileSelection : Screen (
        route = "profileSelection",
        title = "Logout",
        icon = Icons.Outlined.ExitToApp
    )

    // Bottom Nav Bar
    object Profile : Screen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Library : Screen(
        route = "library",
        title = "Library",
        icon = Icons.Default.List
    )
    // Side Bar

    object Settings : Screen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )

    object Account : Screen(
        route = "account",
        title = "Account",
        icon = Icons.Default.AccountCircle
    )

    // Library screens
    object Favorites : Screen(
        route = "favorites",
        title = "Favorites",
        icon = Icons.Default.Favorite
    )

    object Watched : Screen(
        route = "watched",
        title = "Watched",
        icon = Icons.Default.Check
    )

    object Planned : Screen(
        route = "planned",
        title = "Plan to Watch",
        icon = Icons.Default.Star
    )

    // Other
    object Add : Screen(
        route = "add",
        title = "Add",
        icon = Icons.Default.Add
    )

    object AddProfile : Screen(
        route = "addProfile",
        title = "AddProfile",
        icon = Icons.Default.Add
    )
}

fun getSideScreens(): List<Screen>{
    return listOf(Screen.Account/*, Screen.Settings*/)
}