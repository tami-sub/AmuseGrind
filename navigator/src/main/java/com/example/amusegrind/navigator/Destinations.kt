package com.example.amusegrind.navigator

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.navArgument

object LoginDestination : NavigationDestination {
    override fun route(): String = "login"
}

object HomeDestination : NavigationDestination {
    override fun route(): String = "home"
}

object RecorderDestination : NavigationDestination {
    override fun route(): String = "recorder"
}

object ChatDestination : NavigationDestination {
    override fun route(): String = "chat"
}

object ChatMessagesDestination : NavigationDestination {

    private const val ROUTE = "messages"
    private const val ID = "id"

    override val arguments = listOf(
        navArgument(ID) {
            type = NavType.StringType
        }
    )

    override fun route() = "$ROUTE/{$ID}"

    fun createRoute(ID: String) =
        "$ROUTE/${ID}"

    fun extractId(savedStateHandle: SavedStateHandle): String =
      savedStateHandle[ID]!!
}

object SettingsDestination : NavigationDestination {
    override fun route(): String = "settings"
}
