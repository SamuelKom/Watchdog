package at.ac.fhcampuswien.watchdog.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.utils.BotNavBar
import at.ac.fhcampuswien.watchdog.utils.SideBar
import at.ac.fhcampuswien.watchdog.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    navController: NavController = rememberNavController(), homeViewModel: HomeViewModel
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(modifier = Modifier, navController = navController, items = getSideScreens(), scaffoldState = scaffoldState) },
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(text = "Account")
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