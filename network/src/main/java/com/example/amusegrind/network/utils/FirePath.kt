package com.example.amusegrind.network.utils

import com.example.amusegrind.network.domain.entities.audio.AudioType
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


object FirePath {
    private val myUid: String
        get() = Firebase.auth.uid ?: ""

    fun getUserInfo(uid: String = myUid): String =
        "users/$uid/basic-data"

    fun getAllUsersPath() = "users"

    fun getAllUserBasicDataPath() = "basic-data"

    fun getAllAudiosPath(): String = "audios"

    fun getMyLikedAudios(): String = "users/$myUid/liked-audios"

    fun getUserAudios(uid: String, audioType: AudioType) = "users/$uid/$audioType"

    fun getMessagesPath() = "messages"
}