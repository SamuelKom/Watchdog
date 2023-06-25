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
        drawerBackgroundColor = Color(0xFF19191A),
        bottomBar = { BotNavBar(navController = navController, scaffoldState = scaffoldState) },
        backgroundColor = Color(0xFF19191A)
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(Color.Black),
            verticalArrangement = Arrangement.spacedBy(7.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Spacer(modifier = Modifier.height(25.dp))

            CircleWithLetter(letter = "M", color = Color.Blue)

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 40.dp)
            ) {
                Text(
                    text = "Maximilian",
                    color = Color.White,
                    fontSize = 25.sp
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier.padding(1.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextColumn(number = 13, label = "Favourites")
                TextColumn(number = 73, label = "Watched")
                TextColumn(number = 7, label = "Planned")
            }

            Spacer(modifier = Modifier.height(18.dp))

            var genres: List<String> = listOf("Action", "Comedy", "Horror")
            FavouriteGenresRow(genres = genres)
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

            IconButton(
                onClick = { },
                modifier = Modifier.padding(5.dp).size(17.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            }
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

    var modifier: Modifier = if (brush != null) Modifier.background(brush, shape = CircleShape)
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