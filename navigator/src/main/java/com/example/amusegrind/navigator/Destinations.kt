package com.example.amusegrind.navigator

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
