package com.example.amusegrind.network

import com.example.amusegrind.network.data.UserRepo
import com.example.amusegrind.network.domain.entities.user.User
import com.example.amusegrind.network.domain.entities.audio.AudioType
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
import com.example.amusegrind.network.utils.FirePath
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("EXPERIMENTAL_API_USAGE")
@Singleton
class UserRepoImpl @Inject constructor() : UserRepo {
    private val fireAuth: FirebaseAuth = Firebase.auth
    private val firePath: FirePath = FirePath
    private val realtimeDatabase: FirebaseDatabase = Firebase.database

    override fun doesDeviceHaveAnAccount() = fireAuth.currentUser != null

    override suspend fun getUserProfile(uid: String?): Flow<Result<User?>> = flow {
        val userRef = realtimeDatabase.getReference(firePath.getUserInfo(uid ?: ""))
        val snapshot = userRef.get().await()
        val user = snapshot.getValue<User>() ?: throw NoSuchElementException("User not found")
        emit(Result.success(user))
    }.catch { e ->
        emit(Result.failure(e))
    }

    override suspend fun isUserInRealtimeDatabase(uid: String): Flow<Boolean> = flow<Boolean> {
        realtimeDatabase.getReference(firePath.getUserInfo(uid)).get().await().getValue<User>()
            ?: throw NoSuchElementException("User not found")
        emit(true)
    }.catch {
        emit(false)
    }

    override suspend fun addUserToDatabase(
        user: User,
    ) {
        realtimeDatabase
            .getReference(firePath.getUserInfo(Firebase.auth.uid ?: ""))
            .setValue(user)
            .await()
    }

    override suspend fun getUserAudios(
        uid: String?,
        audioType: AudioType
    ): Flow<Result<List<RemoteAudio?>>> {
        val listOfUseraudioId = realtimeDatabase
            .getReference(firePath.getUserAudios(uid ?: "", audioType))
            .get()
            .await()
            .getValue<Map<String, String>>()
            ?.values
            ?.toList() ?: listOf()

        val allAudios = realtimeDatabase.getReference(firePath.getAllAudiosPath())
        return flow {
            emit(Result.success(listOfUseraudioId.map { audioId ->
                allAudios.child(audioId).get().await().getValue<RemoteAudio>()
            }))
        }.catch { e -> emit(Result.failure(e)) }
    }
}