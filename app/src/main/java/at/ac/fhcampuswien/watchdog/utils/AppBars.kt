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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import at.ac.fhcampuswien.watchdog.viewmodels.LibraryViewModel
import kotlinx.coroutines.launch

@Composable
fun BotNavBar(navController: NavController, scaffoldState: ScaffoldState) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scope = rememberCoroutineScope()

    BottomAppBar{
        BottomNavigationItem(label = { Text(text = Screen.Profile.title) }, icon = {
            Icon(
                imageVector = Screen.Profile.icon, contentDescription = "Navigation Icon"
            )
        }, selected = currentDestination?.hierarchy?.any {
            it.route == Screen.Profile.route
        } == true, onClick = {
            scope.launch { scaffoldState.drawerState.open() }
        })
        BottomNavigationItem(label = { Text(text = Screen.Home.title) },
            icon = { Icon(imageVector = Screen.Home.icon, contentDescription = "Navigation Icon") },
            selected = currentDestination?.hierarchy?.any {
                it.route == Screen.Home.route
            } == true,
            onClick = { navController.popBackStack(route = Screen.Home.route, inclusive = false) })
        BottomNavigationItem(label = { Text(text = Screen.Library.title) }, icon = {
            Icon(
                imageVector = Screen.Library.icon, contentDescription = "Navigation Icon"
            )
        }, selected = currentDestination?.hierarchy?.any {
            it.route == Screen.Library.route
        } == true, onClick = {
            navController.popBackStack(route = Screen.Home.route, inclusive = false)
            navController.navigate(Screen.Library.route)
        })
    }
}

@Composable
fun SideBar(
    modifier: Modifier,
    navController: NavController,
    items: List<Screen>,
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Some Tile")
    }
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    scope.launch { scaffoldState.drawerState.close() }
                    navController.popBackStack(route = Screen.Home.route, inclusive = false)
                    navController.navigate(item.route)
                }) {
                Icon(imageVector = item.icon, contentDescription = "Menu Icon")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.title, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun LibraryTopBar(modifier: Modifier, libraryViewModel: LibraryViewModel) {
    TopAppBar(
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        Row(
            modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                //Icon(imageVector = Screen.Favorites.icon, contentDescription = Screen.Favorites.title)
                Text(text = Screen.Favorites.title, Modifier.clickable{
                    libraryViewModel.changeList(0)
                })

            }

            Column {
                //Icon(imageVector = Screen.Completed.icon, contentDescription = Screen.Completed.title)
                Text(text = Screen.Completed.title, Modifier.clickable{
                    libraryViewModel.changeList(1)
                })
            }

            Column {
                //Icon(imageVector = Screen.Planned.icon, contentDescription = Screen.Planned.title)
                Text(text = Screen.Planned.title, Modifier.clickable{
                    libraryViewModel.changeList(2)
                })
            }
        }
    }
}

@Composable
fun SearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Search",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(modifier = Modifier.alpha(ContentAlpha.medium), onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (text.isNotEmpty()) {
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
    }
}