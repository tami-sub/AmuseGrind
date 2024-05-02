package com.example.amusegrind.presentation

import android.annotation.SuppressLint
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.amusegrind.auth.domain.AuthState
import com.example.amusegrind.navigation.addComposableDestinations
import com.example.amusegrind.navigator.ChatDestination
import com.example.amusegrind.navigator.HomeDestination
import com.example.amusegrind.navigator.LoginDestination
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.navigator.NavigatorEvent
import com.example.amusegrind.navigator.RecorderDestination
import com.example.amusegrind.navigator.SettingsDestination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navigator: Navigator) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val state = mainViewModel.state.collectAsState().value
    LaunchedEffect(navController) {
        navigator.destinations.collect {
            mainViewModel.getAuthState()
            when (val event = it) {
                is NavigatorEvent.NavigateUp -> navController.navigateUp()
                is NavigatorEvent.NavigateUpTo -> {
                    navController.popBackStack(
                        event.route,
                        event.inclusive,
                    )
                    mainViewModel.saveCurrentDestination(event.route)
                }

                is NavigatorEvent.Directions -> {
                    navController.navigate(
                        event.destination,
                        event.builder,
                    )
                    mainViewModel.saveCurrentDestination(event.destination)
                }

                is NavigatorEvent.Replace -> {
                    navController.navigate(
                        event.route,
                        builder = {
                            navController.currentDestination?.route?.let { currentRoute ->
                                popUpTo(currentRoute) { inclusive = true }
                            }
                        },
                    )
                    mainViewModel.saveCurrentDestination(event.route)
                }

                is NavigatorEvent.ReplaceRoot -> {
                    navController.navigate(
                        event.route,
                        builder = {
                            popUpTo(0) {
                                inclusive = true
                            }
                        },
                    )
                    mainViewModel.saveCurrentDestination(event.route)
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (state.auth == AuthState.AUTHORIZED) {
                BottomNavigationBar(mainViewModel, state)
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = if (state.auth == AuthState.AUTHORIZED) HomeDestination.route() else LoginDestination.route(),
            builder = { addComposableDestinations() },
        )
    }
}

@Composable
fun BottomNavigationBar(mainViewModel: MainViewModel, state: MainState) {
    val currentRoute = state.currentDestinationRoute
    BottomNavigation {
        BottomNavigationItem(
            icon = {
                Icon(
                    Icons.Default.Home, contentDescription = null,
                    tint = if (currentRoute == HomeDestination.route()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            },
            selected = currentRoute == HomeDestination.route(),
            onClick = { mainViewModel.navigateToHome() }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    Icons.Default.Add, contentDescription = null,
                    tint = if (currentRoute == RecorderDestination.route()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            },
            selected = currentRoute == RecorderDestination.route(),
            onClick = { mainViewModel.navigateToRecordAudio() }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    Icons.Default.MailOutline, contentDescription = null,
                    tint = if (currentRoute == ChatDestination.route()) {
                        Color.White
                    } else {
                        Color.Black
                    },
                )
            },
            selected = currentRoute == ChatDestination.route(),
            onClick = { mainViewModel.navigateToChat() },
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = if (currentRoute == SettingsDestination.route()) {
                        Color.White
                    } else {
                        Color.Black
                    },
                )
            },
            selected = currentRoute == SettingsDestination.route(),
            onClick = { mainViewModel.navigateToSettings() },
        )
    }
}
