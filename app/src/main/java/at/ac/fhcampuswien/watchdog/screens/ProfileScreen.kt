package at.ac.fhcampuswien.watchdog.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.ac.fhcampuswien.watchdog.R
import at.ac.fhcampuswien.watchdog.database.UserDatabase
import at.ac.fhcampuswien.watchdog.database.UserRepository
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.navigation.Navigation
import at.ac.fhcampuswien.watchdog.viewmodels.ProfileViewModel
import at.ac.fhcampuswien.watchdog.viewmodels.ProfileViewModelFactory


@Composable
fun CheckLogin() {
    val sharedPrefs = LocalContext.current.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    //sharedPrefs.edit().putString("user", "Max").apply()
    sharedPrefs.edit().remove("user").apply()
    val user = sharedPrefs.getString("user", null)

    if (user == null) {

        ProfileScreen()
    } else {
        println(user)
        Navigation()
    }
}

@Composable
fun ProfileScreen() {
    val db = UserDatabase.getDatabase(LocalContext.current)
    val repository = UserRepository(userDao = db.userDao())

    val profileFactory = ProfileViewModelFactory(repository)
    val profileViewModel: ProfileViewModel = viewModel(factory = profileFactory)

    profileViewModel.addUser(
        User(
            name = "Oliver",
            color = Color.Blue.toArgb(),
            favGenres = listOf("Anime", "Thriller").toString(),
            theme = "black"
        )
    )
    profileViewModel.addUser(
        User(
            name = "Niklas",
            color = Color.Red.toArgb(),
            favGenres = listOf("Action", "Comedy").toString(),
            theme = "black"
        )
    )

    profileViewModel.addUser(
        User(
            name = "Samuel",
            color = Color.LightGray.toArgb(),
            favGenres = listOf("Action", "Comedy").toString(),
            theme = "black"
        )
    )

    val login = remember { mutableStateOf(false) }

    if (login.value) {
        Navigation()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileSelectionHeading()

            ProfileRow(users = profileViewModel.users)

            LoginButton(onClick = { login.value = true })

            NewProfileButton()
        }
    }
}

@Composable
fun ProfileSelectionHeading() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 60.dp, bottom = 30.dp)
    ) {
        Text(
            text = "Watchdog",
            color = Color.White,
            fontSize = 40.sp,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(R.drawable.dog),
            contentDescription = "Image",
            modifier = Modifier
                .size(45.dp)
                .background(Color.Transparent)
        )
    }
}

@Composable
fun ProfileRow(users: List<User>) {
    //var centeredUserIdx = remember { mutableStateOf(centered) }
    var centeredUserIdx by remember { mutableStateOf(0) }


    if (users.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ProfileChangeButton(
                onClick = { if (centeredUserIdx > 0) centeredUserIdx -= 1 },
                right = false,
                disabled = (centeredUserIdx==0))

            ProfileSelection(
                left = if (centeredUserIdx - 1 < 0) null else users[centeredUserIdx - 1],
                middle = users[centeredUserIdx],
                right = if (centeredUserIdx + 1 > users.size - 1) null else users[centeredUserIdx + 1])

            ProfileChangeButton(
                onClick = { if (centeredUserIdx < users.size - 1) centeredUserIdx += 1 },
                right = true,
                disabled = (centeredUserIdx == (users.size - 1))
            )
        }

    }
}

@Composable
fun ProfileChangeButton(onClick: () -> Unit, right: Boolean, disabled: Boolean) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
    ) {
        Icon(
            imageVector = if (right) Icons.Outlined.KeyboardArrowRight else Icons.Outlined.KeyboardArrowLeft,
            contentDescription = "Back",
            tint = if (!disabled) Color.White else Color.Gray,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun ProfileSelection(left: User?, middle: User, right: User?) {
    Box(
        modifier = Modifier
            .size(75.dp)
            .graphicsLayer { rotationY = -60f }
    ) {
        if (left != null) {
            CircleWithLetter(
                letter = left.name.substring(0, 1),
                color = Color(left.color),
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(left.color),
                        Color.Transparent
                    ),
                    startX = 100f,
                    endX = 0f
                )
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        CircleWithLetter(
            letter = middle.name.substring(0, 1),
            color = Color(middle.color)
        )
        Text(
            text = middle.name,
            color = Color.White,
            fontSize = 27.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 3.dp)
        )
    }

    Box(
        modifier = Modifier
            .size(75.dp)
            .graphicsLayer { rotationY = 60f }
    ) {
        if (right != null) {
            CircleWithLetter(
                letter = right.name.substring(0, 1),
                color = Color(right.color),
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(right.color),
                        Color.Transparent
                    ),
                    startX = 0f,
                    endX = 120f
                )
            )
        }
    }
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .height(35.dp)
            .background(Color.DarkGray, RoundedCornerShape(4.dp)),
    ) {
        Row (
            modifier = Modifier.padding(start = 20.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ){
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = "Icon",
                tint = Color.White,
                modifier = Modifier.padding(top = 2.dp).size(23.dp)
            )
        }
    }
}

@Composable
fun NewProfileButton() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Content of the screen

        IconButton(
            onClick = { /* Button click action */ },
            modifier = Modifier
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(8.dp)
                )

        ) {
            Row(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Plus Icon",
                    tint = Color.White
                )
                Text(
                    text = "Add new Profile",
                    color = Color.Gray
                )
            }
        }
    }
}