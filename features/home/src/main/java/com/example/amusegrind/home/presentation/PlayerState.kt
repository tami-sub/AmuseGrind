package com.example.amusegrind.home.presentation

import com.example.amusegrind.network.domain.entities.audio.RemoteAudio

data class PlayerState(val remoteAudioList: List<RemoteAudio>?)
