package com.example.amusegrind.network.domain.entities.audio

data class RemoteAudio(
    var url: String = "",
    var description: String? = null,
    var duration: Long = -1,
    var audioId: String ="",
    var dateCreated: Long = -1,
    var likes: Long = 0,
    var views: Long = 0,
    var authorUid: String = ""
)