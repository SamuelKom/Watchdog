package at.ac.fhcampuswien.watchdog.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import at.ac.fhcampuswien.watchdog.models.Genre
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchGenresByWatchableType
import at.ac.fhcampuswien.watchdog.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    login: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {

        // Load user ID from system variable
        val sharedPrefs =
            LocalContext.current.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val userID = sharedPrefs.getString("user", null)

        val user : User? =
            if (userID != null) profileViewModel.getUserById(userID)
            else null

        Text(
            text =
            if (user == null) "Create Profile"
            else "Edit Profile",
            color = Color.White,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        var textFieldValue by remember {
            mutableStateOf(user?.name ?: "")
        }
        val selectedColor: MutableState<Color?> = remember {
            mutableStateOf(user?.color?.let { Color(it) })
        }
        val genreList = remember { mutableStateListOf<Genre>() }
        val selectedItems = remember { mutableStateListOf<String>() }

        if (user != null) {
            selectedItems.clear()
            for (genre in user.favGenres.drop(1).dropLast(1).split(",")) {
                selectedItems.add(genre)
            }
        }

        if (genreList.isEmpty())
            fetchGenresByWatchableType("movie", genreList)

        NameRow(textFieldValue = textFieldValue,
            onChange = { newValue -> textFieldValue = newValue})

        ColorRow(selectedColor = selectedColor.value,
            onChange = { color -> selectedColor.value = color })

        GenreSelection(selectedItems = selectedItems,
            addItem = { g -> selectedItems.add(g) },
            removeItem = { g -> selectedItems.remove(g) },
            genres = genreList)

        ButtonRow(
            navController = navController,
            profileViewModel = profileViewModel,
            name = textFieldValue,
            color = selectedColor.value,
            favGenres = selectedItems,
            login = login
        )
    }
}

@Composable
fun RowLabel(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun NameRow(textFieldValue: String, onChange: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RowLabel(text = "Name:")

        TextField(
            value = textFieldValue,
            onValueChange = { newValue -> onChange(newValue) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .border(
                    width = 1.dp,
                    color = if (isFocused) Color.White else Color.Gray,
                    shape = RoundedCornerShape(7.dp)
                )
                .onFocusChanged { isFocused = it.isFocused },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.LightGray
            ),
            singleLine = true
        )
    }
}

@Composable
fun ColorRow(selectedColor: Color?, onChange: (Color) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RowLabel(text = "Color:")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val colors: List<Color> = listOf(
                Color(0xFF64070A), Color(0xFF393D55), Color(0xFF1A203F), Color(0xFF580766), Color(0xFF0E5011)
            )

            for (color in colors) {
                ColorBox(
                    color = color, modifier = Modifier.weight(1f),
                    selected = selectedColor == color
                ) { onChange(color) }
            }
        }
    }
}

@Composable
fun ColorBox(color: Color, modifier: Modifier, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .size(50.dp)
            .background(color, shape = RoundedCornerShape(7.dp))
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Color.White else Color.Gray,
                shape = RoundedCornerShape(7.dp)
            )
            .clickable { onClick() }
    )
}

@Composable
fun GenreSelection(
    genres: List<Genre>,
    selectedItems: List<String>,
    addItem: (String) -> Unit,
    removeItem: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RowLabel(text = "Select 3 genres:")
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(genres) { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(if (selectedItems.contains(item.name)) Color.White else Color.Gray)
                                .clickable{
                                    if (!selectedItems.contains(item.name)) {
                                        if (selectedItems.size < 3) {
                                            addItem(item.name)
                                        }
                                    } else {
                                        removeItem(item.name)
                                    }},
                            contentAlignment = Alignment.Center
                        ) {
                            if(selectedItems.contains(item.name))
                                Icon(Icons.Default.Check, contentDescription = "", tint = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = item.name,
                        color = Color.White,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun ButtonRow(
    navController: NavController, profileViewModel: ProfileViewModel,
    name: String, color: Color?,
    favGenres: List<String>, login: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .weight(1f)

            ) {
                Row(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Plus Icon",
                        tint = Color.Gray
                    )
                    Text(
                        text = "Back",
                        color = Color.Gray
                    )
                }
            }


            val sharedPrefs =
                LocalContext.current.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val userID = sharedPrefs.getString("user", null)
            val coroutineScope = rememberCoroutineScope()

            IconButton(
                onClick = {
                    if (name.isNotEmpty() && color != null && favGenres.isNotEmpty()) {
                        println(name)
                        coroutineScope.launch {
                            val user = User(
                                name = name,
                                color = color.toArgb(),
                                favGenres = favGenres.joinToString(prefix = "[", postfix = "]"),
                                theme = "black"
                            )

                            if (userID == null) {
                                profileViewModel.addUser(user)
                                login(user.id)
                            } else {
                                user.id = userID
                                profileViewModel.updateUser(user)
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .background(
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .weight(1f)

            ) {
                Row(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Text(
                        text =
                        if (userID == null) "Login"
                        else "Save",
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}