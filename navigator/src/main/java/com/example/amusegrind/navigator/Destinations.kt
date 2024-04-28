package com.example.amusegrind.navigator

object LoginDestination : NavigationDestination {
    override fun route(): String = "login"
}

object HomeDestination : NavigationDestination {
    override fun route(): String = "home"
}

object RecordAudioDestination : NavigationDestination {
    override fun route(): String = "record"
}

object PreviewAudioDestination : NavigationDestination {
    override fun route(): String = "preview"
}

object ChatDestination : NavigationDestination {
    override fun route(): String = "chat"
}

object SettingsDestination : NavigationDestination {
    override fun route(): String = "settings"
}
