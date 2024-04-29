package com.example.amusegrind.network

import com.example.amusegrind.network.domain.entities.audio.VideoType
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


object FirePath {
    private val myUid: String
        get() = Firebase.auth.uid ?: ""

    fun getUserInfo(uid: String = myUid): String =
        "users/$uid/basic-data"

    fun getAllAudiosPath(): String = "audios"

    fun getMyLikedAudios(): String = "users/$myUid/liked-audios"

    fun getUserAudios(uid: String, videoType: VideoType) = "users/$uid/$videoType"

    fun getTakenUsernames(userName: String) = "taken-usernames/$userName"
}