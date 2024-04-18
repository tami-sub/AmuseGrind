package com.example.amusegrind.navigator

import androidx.navigation.NavOptionsBuilder

sealed class NavigatorEvent {
    data object NavigateUp : NavigatorEvent()
    class NavigateUpTo(val route: String, val inclusive: Boolean = false) : NavigatorEvent()
    class Directions(
        val destination: String,
        val builder: NavOptionsBuilder.() -> Unit
    ) : NavigatorEvent()
    class Replace(
        val route: String
    ) : NavigatorEvent()
    class ReplaceRoot(
        val route: String
    ) : NavigatorEvent()
}
