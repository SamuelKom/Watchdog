package at.ac.fhcampuswien.watchdog.screens

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import at.ac.fhcampuswien.watchdog.R
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch


@Composable
fun CheckLogin(navController: NavHostController, profileViewModel: ProfileViewModel, login: (String) -> Unit) {
    /*val sharedPrefs = LocalContext.current.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val user = sharedPrefs.getString("user", null)

    if (user == null) {*/
    ProfileSelectionScreen(
        profileViewModel = profileViewModel,
        navController = navController,
        login = login)
    /*} else {
        navController.popBackStack(route = Screen.Home.route, inclusive = false)
    }*/
}

@Composable
fun ProfileSelectionScreen(
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    login: (String) -> Unit
) {
    /*val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            profileViewModel.addUser(
                User(
                    name = "Oliver",
                    color = Color.Blue.toArgb(),
                    favGenres = listOf("Anime", "Thriller").toString(),
                    theme = "black"
                )
            )
        }
    }*/

    val centeredUserIdx: MutableState<Int> = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileSelectionHeading()

        val users by profileViewModel.users.collectAsState()

        ProfileRow(users = users,
            centeredUserIdx = centeredUserIdx.value,
            centeredChanged = { idx -> centeredUserIdx.value = idx }
        )

        LoginButton(onClick = {
            login(profileViewModel.getUserIdByIdx(centeredUserIdx.value)!!)
            //navController.navigate(route = Screen.Home.route)
        })

        NewProfileButton( onCreateProfileClicked = { navController.navigate(route = Screen.AddProfile.route) })
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
fun ProfileRow(users: List<User>, centeredUserIdx: Int, centeredChanged: (Int) -> Unit) {

    val animationDurationMillis = 500

    //var rotationState = remember { Animatable(0f) }
    var targetRotationValue by remember { mutableStateOf(0f) }
    val rotationState by animateFloatAsState(
        targetValue = targetRotationValue,
        animationSpec =
        if (targetRotationValue != 0f) {
            tween(durationMillis = animationDurationMillis)
        } else {
            TweenSpec(durationMillis = 0)
        }
    )

    var targetOffsetValue by remember { mutableStateOf(0f) }
    val offsetState by animateFloatAsState(
        targetValue = targetOffsetValue,
        animationSpec =
        if (targetOffsetValue != 0f) {
            tween(durationMillis = animationDurationMillis)
        } else {
            TweenSpec(durationMillis = 0)
        }
    )

    var targetSizeDiffValue by remember { mutableStateOf(0f) }
    val sizeDiffState by animateFloatAsState(
        targetValue = targetSizeDiffValue,
        animationSpec =
        if (targetSizeDiffValue != 0f) {
            tween(durationMillis = animationDurationMillis)
        } else {
            TweenSpec(durationMillis = 0)
        }
    )

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(rotationState) {
        if (rotationState == targetRotationValue) {
            if (rotationState > 0) {
                centeredChanged(centeredUserIdx - 1)
            } else if (rotationState < 0) {
                centeredChanged(centeredUserIdx + 1)
            }

            targetOffsetValue = 0f
            targetRotationValue = 0f
            targetSizeDiffValue = 0f
        }
    }

    LaunchedEffect(centeredUserIdx) {
        //offsetMiddleState.animateTo(0f, animationSpec = TweenSpec(durationMillis = 0))

    }

    if (users.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(180.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ProfileChangeButton(
                onClick = { if (centeredUserIdx > 0) {
                    coroutineScope.launch {
                        /*launch {
                            rotationState.animateTo(
                                60f,
                                animationSpec = tween(durationMillis = 800)
                            )
                        }*/
                        targetRotationValue = 60f
                        targetOffsetValue = 75f
                        targetSizeDiffValue = 40f
                    }
                }},
                right = false,
                disabled = (centeredUserIdx==0))

            ProfileSelection(
                offset = offsetState,
                rotation = rotationState,
                sizeDiff = sizeDiffState,
                left = if (centeredUserIdx - 1 < 0) null else users[centeredUserIdx - 1],
                middle = users[centeredUserIdx],
                right = if (centeredUserIdx + 1 > users.size - 1) null else users[centeredUserIdx + 1])

            ProfileChangeButton(
                onClick = { if (centeredUserIdx < users.size - 1) {
                    coroutineScope.launch {
                        targetRotationValue = -60f
                        targetOffsetValue = -75f
                        targetSizeDiffValue = 40f
                    }
                }},
                right = true,
                disabled = (centeredUserIdx == (users.size - 1))
            )
        }

    }
}

@Composable
fun ProfileChangeButton(onClick: () -> Unit, right: Boolean, disabled: Boolean) {
    Box (
        contentAlignment = if (right) Alignment.CenterEnd else Alignment.CenterStart
            ){
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = if (right) Icons.Outlined.KeyboardArrowRight else Icons.Outlined.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = if (!disabled) Color.White else Color.Gray,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun ProfileSelection(
    left: User?,
    middle: User,
    right: User?,
    rotation: Float,
    sizeDiff: Float,
    offset: Float
) {
    Box(
        modifier = Modifier
            .size(if (rotation <= 0) 75.dp else (75 + sizeDiff).dp)
            .graphicsLayer { rotationY = -60f + rotation }
            .offset(x = offset.dp)
    ) {
        if (left != null && rotation >= 0) {
            CircleWithLetter(
                letter = left.name.substring(0, 1),
                color = Color(left.color),
                brush = if (rotation < 45) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(left.color),
                            Color.Transparent
                        ),
                        startX = 100f,
                        endX = 0f
                    )
                } else null
            )
        }
    }

    CenteredProfile(middle = middle, rotation = rotation, offset = offset, sizeDiff = sizeDiff)

    Box(
        modifier = Modifier
            .size(if (rotation >= 0) 75.dp else (75 + sizeDiff).dp)
            .graphicsLayer { rotationY = 60f + rotation }
            .offset(x = offset.dp)
    ) {
        if (right != null && rotation <= 0) {
            CircleWithLetter(
                letter = right.name.substring(0, 1),
                color = Color(right.color),
                brush = if (rotation > -45) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(right.color),
                            Color.Transparent
                        ),
                        startX = 0f,
                        endX = 120f
                    )
                } else null
            )
        }
    }
}

@Composable
fun CenteredProfile(middle: User, rotation: Float, offset: Float, sizeDiff: Float) {
   Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(x = offset.dp)
    ) {
       if (rotation == 0f) {
           Spacer(modifier = Modifier.height(32.dp))
       }

        Box(
            modifier = Modifier
                .graphicsLayer { this.rotationY = rotation }
                .size((115 - sizeDiff).dp)
        ) {
            CircleWithLetter(
                letter = middle.name.substring(0, 1),
                color = Color(middle.color),
            )
        }

        if (rotation == 0f) {
            Text(
                text = middle.name,
                color = Color.White,
                fontSize = 27.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 3.dp)
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
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(23.dp)
            )
        }
    }
}

@Composable
fun NewProfileButton(onCreateProfileClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Content of the screen

        IconButton(
            onClick = { onCreateProfileClicked() },
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

        ) {
            Row(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
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