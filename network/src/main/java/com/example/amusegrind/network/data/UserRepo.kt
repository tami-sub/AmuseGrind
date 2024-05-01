package com.example.amusegrind.network.data

import android.net.Uri
import com.example.amusegrind.network.domain.entities.User
import com.example.amusegrind.network.domain.entities.audio.AudioType
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface UserRepo {

    fun doesDeviceHaveAnAccount(): Boolean

    suspend fun getUserProfile(uid: String?): Flow<Result<User?>>

    suspend fun isUserInRealtimeDatabase(uid: String): Flow<Boolean>

    suspend fun addUserToDatabase(
        user: User
    )


    suspend fun getUserAudios(uid: String?, audioType: AudioType): Flow<Result<List<RemoteAudio?>>>
}
