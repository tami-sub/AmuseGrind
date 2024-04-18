package com.example.amusegrind.network

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NavigatorImpl @Inject constructor() : Navigator {

    private val navigationEvents = Channel<NavigatorEvent>()
    override val destinations = navigationEvents.receiveAsFlow()

    override fun navigateUp(): Boolean =
        navigationEvents.trySend(NavigatorEvent.NavigateUp).isSuccess

    override fun navigateUpTo(route: String, inclusive: Boolean): Boolean =
        navigationEvents.trySend(NavigatorEvent.NavigateUpTo(route, inclusive)).isSuccess

    override fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit): Boolean =
        navigationEvents.trySend(NavigatorEvent.Directions(route, builder)).isSuccess

    override fun replace(route: String): Boolean =
        navigationEvents.trySend(NavigatorEvent.Replace(route)).isSuccess

    override fun replaceRoot(route: String): Boolean =
        navigationEvents.trySend(NavigatorEvent.ReplaceRoot(route)).isSuccess
}
