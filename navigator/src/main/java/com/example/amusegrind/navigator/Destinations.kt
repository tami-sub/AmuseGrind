package com.example.amusegrind.navigator

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

object SettingsDestination : NavigationDestination {
    override fun route(): String = "settings"
}
