package com.example.amusegrind.network.domain.entities.audio

enum class AudioType {
    LIKED {
        override fun toString(): String = "liked-audios"
    },
    PRIVATE {
        override fun toString(): String = "private-audios"
    },
    PUBLIC {
        override fun toString(): String = "public-audios"
    }
}