package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.utils.*
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel

@Composable
fun LibraryScreen(
    navController: NavController = rememberNavController(), homeViewModel: HomeViewModel
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(modifier = Modifier, navController = navController, items = getSideScreens()) },
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) },
        topBar = { LibraryTopBar(modifier = Modifier, navController = navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(text = "Library")
        }
    }
}