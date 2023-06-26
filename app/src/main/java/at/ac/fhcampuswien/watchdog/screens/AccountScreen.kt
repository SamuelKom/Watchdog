package at.ac.fhcampuswien.watchdog.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.utils.BotNavBar
import at.ac.fhcampuswien.watchdog.utils.SideBar
import at.ac.fhcampuswien.watchdog.viewmodels.LibraryViewModel
import at.ac.fhcampuswien.watchdog.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    navController: NavController = rememberNavController(),
    profileViewModel: ProfileViewModel,
    libraryViewModel: LibraryViewModel,
    logout: () -> Unit,
    user: User
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    var editClicked by remember { mutableStateOf(false) }


    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SideBar(modifier = Modifier, navController = navController, items = getSideScreens(), scaffoldState = scaffoldState, logout = logout) },
        drawerBackgroundColor = Color(0xFF19191A),
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState, color = Color(user.color)) },
        backgroundColor = Color(0xFF19191A)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.spacedBy(7.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Spacer(modifier = Modifier.height(25.dp))

            CircleWithLetter(letter = user.name.substring(0, 1), color = Color(user.color))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 40.dp)
            ) {
                Text(
                    text = user.name,
                    color = Color.White,
                    fontSize = 25.sp
                )
                IconButton(
                    onClick = { navController.navigate(Screen.AddProfile.route) },
                    modifier = Modifier.padding(1.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }

                IconButton(
                    onClick = {
                        scope.launch {
                            profileViewModel.delete(user)
                            logout()
                        }},
                    modifier = Modifier.padding(1.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val numbers = libraryViewModel.getListSizesFWP()

                TextColumn(number = numbers.first, label = "Favourites")
                TextColumn(number = numbers.second, label = "Watched")
                TextColumn(number = numbers.third, label = "Planned")
            }

            Spacer(modifier = Modifier.height(18.dp))

            //val genres: List<String> = listOf("Action", "Comedy", "Horror")
            FavouriteGenresRow(genres = user.favGenres.drop(1).dropLast(1).split(","))
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

@Composable
fun FavouriteGenresRow(genres: List<String>) {
    Column (
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(start = 20.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favourite Genres",
                color = Color.White,
                fontSize = 19.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (genre in genres) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Gray
                ) {
                    Text(
                        text = genre,
                        color = Color.White,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TextColumn(number: Int, label: String) {
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number.toString(),
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CircleWithLetter(letter: String, color: Color, brush: Brush? = null) {

    val modifier: Modifier = if (brush != null) Modifier.background(brush, shape = CircleShape)
    else Modifier.background(color, shape = CircleShape)

    Box(
        modifier = modifier.size(115.dp)
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}