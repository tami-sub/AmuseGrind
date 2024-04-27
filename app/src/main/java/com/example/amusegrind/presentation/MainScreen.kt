package com.example.amusegrind.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.amusegrind.auth.domain.AuthState
import com.example.amusegrind.navigation.addComposableDestinations
import com.example.amusegrind.navigator.LoginDestination
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.navigator.NavigatorEvent

@Composable
fun MainScreen(navigator: Navigator) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val state = mainViewModel.state.collectAsState().value
    mainViewModel.getAuthState()
    LaunchedEffect(navController) {
        navigator.destinations.collect {
            when (val event = it) {
                is NavigatorEvent.NavigateUp -> navController.navigateUp()
                is NavigatorEvent.NavigateUpTo -> {
                    navController.popBackStack(
                        event.route,
                        event.inclusive,
                    )
                }

                is NavigatorEvent.Directions -> navController.navigate(
                    event.destination,
                    event.builder,
                )

                is NavigatorEvent.Replace -> navController.navigate(
                    event.route, builder = {
                        navController.currentDestination?.route?.let { currentRoute ->
                            popUpTo(currentRoute) { inclusive = true }
                        }
                    }
                )

                is NavigatorEvent.ReplaceRoot -> navController.navigate(
                    event.route, builder = { popUpTo(0) { inclusive = true } }
                )
            }
        }
    }

    // В конце вернуть нормальную логику
    NavHost(
        navController = navController,
        startDestination = if (state.auth == AuthState.AUTHORIZED) LoginDestination.route() else LoginDestination.route(),
        builder = { addComposableDestinations() }
    )
}
