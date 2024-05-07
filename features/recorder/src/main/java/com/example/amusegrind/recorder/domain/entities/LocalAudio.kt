package com.example.amusegrind.recorder.domain.entities

data class LocalAudio(
    var filePath: String?,
    val duration: Long?,
    val dateCreated: String?
)
