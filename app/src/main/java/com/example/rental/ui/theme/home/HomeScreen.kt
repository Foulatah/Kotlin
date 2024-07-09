package net.pane.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import net.pane.navigation.ROUTE_HOME
import net.pane.navigation.ROUTE_LOGIN


class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            HomeScreen(navController = navController)
        }
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var isDrawerOpen by remember { mutableStateOf(false) }

    // State variables for input fields
    var rent by remember { mutableStateOf(TextFieldValue()) }
    var rentArrears by remember { mutableStateOf(TextFieldValue()) }
    var water by remember { mutableStateOf(TextFieldValue()) }
    var garbage by remember { mutableStateOf(TextFieldValue()) }
    var totalAmount by remember { mutableStateOf(TextFieldValue()) }

    // Activity result launcher for dialing phone number
    val callLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Handle result here if needed
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Pane")
                },
                navigationIcon = {
                    if (!isDrawerOpen) {
                        IconButton(onClick = { isDrawerOpen = true }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_LOGIN) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    }) {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = "Account",
                            tint = Color.White
                        )
                    }
                },

                )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (isDrawerOpen) {
                            isDrawerOpen = false
                        }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = rent,
                        onValueChange = { rent = it },
                        label = { Text("Rent") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rentArrears,
                        onValueChange = { rentArrears = it },
                        label = { Text("Rent Arrears") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = water,
                        onValueChange = { water = it },
                        label = { Text("Water") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = garbage,
                        onValueChange = { garbage = it },
                        label = { Text("Garbage") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = totalAmount,
                        onValueChange = { totalAmount = it },
                        label = { Text("Total Amount") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Button to initiate phone call
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:+254740093608")
                            callLauncher.launch(intent)
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Call")
                    }

                    Text(
                        text = "You're welcome",
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                }
            }
        },
        bottomBar = { BottomBar(navController = navController) }
    )

    // Animated drawer
    AnimatedDrawer(
        isOpen = isDrawerOpen,
        onClose = { isDrawerOpen = false }
    )
}

@Composable
fun AnimatedDrawer(isOpen: Boolean, onClose: () -> Unit) {
    val drawerWidth = remember { Animatable(if (isOpen) 250f else 0f) }

    LaunchedEffect(isOpen) {
        drawerWidth.animateTo(
            if (isOpen) 250f else 0f,
            animationSpec = tween(durationMillis = 300)
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(drawerWidth.value.dp),
        color = Color.LightGray
    ) {
        Column {
            DrawerItem(text = "Drawer Item 1")
            DrawerItem(text = "Drawer Item 2")
            DrawerItem(
                text = "Drawer Item 3",
                onClick = { /* Handle click */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Home")
        }
    }
}

@Composable
fun DrawerItem(text: String, onClick: () -> Unit = {}) {
    Text(
        text = text,
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val selectedIndex = remember { mutableStateOf(0) }

    BottomNavigation(
        elevation = 10.dp,
        backgroundColor = Color(0xff0FB06A)
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, "", tint = Color.White) },
            label = { Text(text = "Home", color = Color.White) },
            selected = selectedIndex.value == 0,
            onClick = {
                // Handle click action
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Favorite, "", tint = Color.White) },
            label = { Text(text = "Favorite", color = Color.White) },
            selected = selectedIndex.value == 1,
            onClick = {
                // Handle click action
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Person, "", tint = Color.White) },
            label = { Text(text = "Students", color = Color.White) },
            selected = selectedIndex.value == 2,
            onClick = {
                // Handle click action
            }
        )
    }
}