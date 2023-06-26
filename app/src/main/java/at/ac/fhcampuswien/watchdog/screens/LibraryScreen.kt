package at.ac.fhcampuswien.watchdog.screens

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.utils.*
import at.ac.fhcampuswien.watchdog.viewmodels.LibraryViewModel
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(
    navController: NavController = rememberNavController(),
    libraryViewModel: LibraryViewModel,
    logout: () -> Unit
) {
    val currentList by libraryViewModel.currentList

    val list = libraryViewModel.currentWatchables.value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    println("In LibraryScreen")

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(modifier = Modifier, navController = navController, items = getSideScreens(), scaffoldState = scaffoldState, logout = logout) },
        drawerBackgroundColor = Color(0xFF19191A),
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) },
        topBar = { LibraryTopBar(modifier = Modifier, libraryViewModel = libraryViewModel) },
        backgroundColor = Color(0xFF19191A)
    ) { padding ->
        println(padding)
        LibraryWatchableList(listTitle = currentList, watchableList = list, viewModel = libraryViewModel)
    }

    BackHandler {
        if (scaffoldState.drawerState.isOpen) {
            scope.launch { scaffoldState.drawerState.close() }
        } else {
            navController.popBackStack()
        }
    }
}