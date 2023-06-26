package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.viewmodels.ProfileViewModel

@Composable
fun EditProfileScreen(navController: NavController, profileViewModel: ProfileViewModel) {
    Column (
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ){
        Text(
            text = "Create Profile",
            color = Color.White,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        var textFieldValue by remember { mutableStateOf("") }
        val selectedColor: MutableState<Color?> = remember { mutableStateOf(null) }

        NameRow(textFieldValue = textFieldValue,
            onChange = {newValue -> textFieldValue = newValue})

        ColorRow(selectedColor = selectedColor.value,
            onChange = { color -> selectedColor.value = color})

        ButtonRow(
            navController = navController,
            profileViewModel = profileViewModel,
            name = textFieldValue,
            color = selectedColor.value)
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

    Column (
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
fun ButtonRow(navController: NavController, profileViewModel: ProfileViewModel,
              name: String, color: Color?) {
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

            IconButton(
                onClick = {
                    profileViewModel.addUser(
                        User(
                            name = name,
                            color = color!!.toArgb(),
                            favGenres = "",
                        theme = "black"
                        )
                    );
                    navController.navigate(route = Screen.Home.route) },
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