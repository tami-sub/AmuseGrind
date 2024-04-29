package com.example.amusegrind.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.amusegrind.auth.presentation.LoginScreen
import com.example.amusegrind.home.presentation.HomeScreen
import com.example.amusegrind.navigator.HomeDestination
import com.example.amusegrind.navigator.LoginDestination
import com.example.amusegrind.navigator.NavigationDestination
import com.example.amusegrind.navigator.SettingsDestination
import com.example.amusegrind.settings.presentation.SettingsScreen

private val destinations: Map<NavigationDestination, @Composable () -> Unit> =
    mapOf(
        LoginDestination to { LoginScreen() },
        SettingsDestination to { SettingsScreen() },
        HomeDestination to { HomeScreen()}
    )

fun NavGraphBuilder.addComposableDestinations() {
    destinations.forEach { entry ->
        val destination = entry.key
        composable(destination.route(), destination.arguments, destination.deepLinks) {
            entry.value()
        }
    }
}
