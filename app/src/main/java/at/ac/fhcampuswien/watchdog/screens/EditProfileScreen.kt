package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch
import org.w3c.dom.Text

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
        Text(
            text = "Create Profile",
            color = Color.White,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        var textFieldValue by remember { mutableStateOf("") }
        val selectedColor: MutableState<Color?> = remember { mutableStateOf(null) }

        NameRow(textFieldValue = textFieldValue,
            onChange = { newValue -> textFieldValue = newValue })

        ColorRow(selectedColor = selectedColor.value,
            onChange = { color -> selectedColor.value = color })

        val selectedItems = remember { mutableStateListOf<String>() }
        GenreSelection(selectedItems = selectedItems,
            addItem = { g -> selectedItems.add(g) },
            removeItem = { g -> selectedItems.remove(g) })

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
                Color.Red, Color.Blue, Color.Green, Color.Magenta, Color.Yellow
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
    genres: List<String> = listOf("Action", "Anime", "Comic", "Thriller", "Comedy"),
    selectedItems: List<String>, addItem: (String) -> Unit, removeItem: (String) -> Unit
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
                    /*Checkbox(
                        checked = selectedItems.contains(item),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                if (selectedItems.size < 3) {
                                    addItem(item)
                                }
                            } else {
                                removeItem(item)
                            }
                        },
                        modifier = Modifier.background(Color.DarkGray).padding(0.dp),
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = Color.White
                        )
                    )*/
                    Card(
                        modifier = Modifier
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(if (selectedItems.contains(item)) Color.White else Color.Gray)
                                .clickable{
                                    if (!selectedItems.contains(item)) {
                                        if (selectedItems.size < 3) {
                                            addItem(item)
                                        }
                                    } else {
                                        removeItem(item)
                                    }},
                            contentAlignment = Alignment.Center
                        ) {
                            if(selectedItems.contains(item))
                                Icon(Icons.Default.Check, contentDescription = "", tint = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = item,
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


            val coroutineScope = rememberCoroutineScope()

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val user = User(
                            name = name,
                            color = color!!.toArgb(),
                            favGenres = favGenres.toString(),
                            theme = "black"
                        )

                        profileViewModel.addUser(user)

                        login(user.id)
                        //navController.navigate(route = Screen.Home.route)
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
                        text = "Login",
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}