package com.example.amusegrind.navigator

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface Navigator {
    fun navigateUp(): Boolean
    fun navigateUpTo(route: String, inclusive: Boolean = false): Boolean
    fun replace(route: String): Boolean
    fun replaceRoot(route: String): Boolean
    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = {}): Boolean

    val destinations: Flow<NavigatorEvent>
}
