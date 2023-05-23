package at.ac.fhcampuswien.watchdog.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import at.ac.fhcampuswien.watchdog.screens.Screen
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BotNavBar(navController: NavController, scaffoldState: ScaffoldState) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scope = rememberCoroutineScope()

    BottomAppBar() {
        BottomNavigationItem(label = { Text(text = Screen.Profile.title) },
            icon = { Icon(imageVector = Screen.Profile.icon, contentDescription = "Navigation Icon") },
            selected = currentDestination?.hierarchy?.any {
                it.route == Screen.Profile.route
            } == true,
            onClick = {
                scope.launch { scaffoldState.drawerState.open() }
            }
        )
        BottomNavigationItem(label = { Text(text = Screen.Home.title) },
            icon = { Icon(imageVector = Screen.Home.icon, contentDescription = "Navigation Icon") },
            selected = currentDestination?.hierarchy?.any {
                it.route == Screen.Home.route
            } == true,
            onClick = { navController.navigate(Screen.Home.route) }
        )
        BottomNavigationItem(label = { Text(text = Screen.Library.title) },
            icon = { Icon(imageVector = Screen.Library.icon, contentDescription = "Navigation Icon") },
            selected = currentDestination?.hierarchy?.any {
                it.route == Screen.Library.route
            } == true,
            onClick = { navController.navigate(Screen.Library.route) }
        )
    }
}

@Composable
fun SideBar(modifier: Modifier, navController: NavController, items: List<Screen>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Header Box")
    }
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navController.navigate(item.route) }) {
                Icon(imageVector = item.icon, contentDescription = "Menu Icon")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.title, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun LibraryTopBar(modifier: Modifier, navController: NavController) {
    TopAppBar(
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                //Icon(imageVector = Screen.Favorites.icon, contentDescription = Screen.Favorites.title)
                Text(text = Screen.Favorites.title, modifier = Modifier.clickable {
                    // call viewmodel function to display list
                })
            }

            Column {
                //Icon(imageVector = Screen.Completed.icon, contentDescription = Screen.Completed.title)
                Text(text = Screen.Completed.title, modifier = Modifier.clickable {
                    // call viewmodel function to display list
                })
            }

            Column {
                //Icon(imageVector = Screen.Planned.icon, contentDescription = Screen.Planned.title)
                Text(text = Screen.Planned.title, modifier = Modifier.clickable {
                    // call viewmodel function to display list
                })
            }
        }
    }
}