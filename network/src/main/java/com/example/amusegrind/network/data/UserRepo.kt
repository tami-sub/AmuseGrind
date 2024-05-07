package com.example.amusegrind.network.data

import com.example.amusegrind.network.domain.entities.user.User
import com.example.amusegrind.network.domain.entities.audio.AudioType
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
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
