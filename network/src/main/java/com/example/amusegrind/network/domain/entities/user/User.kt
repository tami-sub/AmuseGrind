package com.example.amusegrind.network.domain.entities.user

data class User(
    val uid: String? = null,
    var username: String? = null,
    var totalLikes: Long = 0,
    var profilePictureUrl: String? = null,
    var showLikedAudios: Boolean = false
)
