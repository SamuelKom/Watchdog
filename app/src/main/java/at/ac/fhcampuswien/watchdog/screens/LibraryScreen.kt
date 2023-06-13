package at.ac.fhcampuswien.watchdog.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.utils.*
import at.ac.fhcampuswien.watchdog.viewmodels.LibraryViewModel
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(
    navController: NavController = rememberNavController(), libraryViewModel: LibraryViewModel
) {
    val currentMoviesState by libraryViewModel.currentMoviesState.collectAsState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()


    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(modifier = Modifier, navController = navController, items = getSideScreens(), scaffoldState = scaffoldState) },
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) },
        topBar = { LibraryTopBar(modifier = Modifier, libraryViewModel = libraryViewModel) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LibraryList(currentMoviesState)
        }
    }

    BackHandler {
        if (scaffoldState.drawerState.isOpen) {
            scope.launch { scaffoldState.drawerState.close() }
        } else {
            navController.popBackStack()
        }
    }
}