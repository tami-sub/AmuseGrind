package com.example.amusegrind.home.presentation

import com.example.amusegrind.network.domain.entities.audio.RemoteAudio

data class HomeState(
    val remoteAudioList: List<RemoteAudio>?,
    val authorImageUrl: String?,
    val isLiked: Boolean = false
)
