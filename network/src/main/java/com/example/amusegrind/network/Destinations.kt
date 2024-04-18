package com.example.amusegrind.network

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object ExampleDestination : NavigationDestination {
    override fun route(): String = "example"
}

object ExampleDetailDestination : NavigationDestination {
    private const val EXAMPLE_DETAIL_ROUTE = "example_detail"
    const val ID_PARAM = "id"

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(ID_PARAM) {
            type = NavType.IntType
            nullable = false
        }
    )

    override fun route() = "$EXAMPLE_DETAIL_ROUTE/{$ID_PARAM}"
    fun createRoute(id: Int) = "$EXAMPLE_DETAIL_ROUTE/$id"
}

object ExampleSuccessDestination : NavigationDestination {
    override fun route(): String = "example_success"
}

object LoginDestination : NavigationDestination {
    override fun route(): String = "login"
}

object HomeDestination : NavigationDestination {
    override fun route(): String = "home"
}

object recordAudioDestination : NavigationDestination {
    override fun route(): String = "recordAudio"
}

object previewAudioDestination : NavigationDestination {
    override fun route(): String = "previewAudio"
}

object chatDestination : NavigationDestination {
    override fun route(): String = "chat"
}

object personalInfoDestination : NavigationDestination {
    override fun route(): String = "personalInfo"
}
