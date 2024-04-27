package com.example.amusegrind.network.domain.entities

import android.net.Uri

data class User(
    val uid: String? = null,
    var username: String? = null,
    var totalLikes: Long = 0,
    var profilePictureUrl: Uri? = null,
    var showLikedAudios: Boolean = false
)
