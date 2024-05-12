package com.example.amusegrind.network

import android.net.Uri
import android.util.Log
import com.example.amusegrind.network.data.AudiosRepo
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
import com.example.amusegrind.network.domain.entities.audio.AudioType
import com.example.amusegrind.network.utils.FirePath
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudiosRepoImpl @Inject constructor(
    private val realtimeDataBase: FirebaseDatabase,
    private val fireStorage: FirebaseStorage
) : AudiosRepo {

    override suspend fun fetchRandomAudios(): Flow<Result<List<RemoteAudio>>> {
        val reference = realtimeDataBase.getReference(FirePath.getAllAudiosPath())
        val snapshot = reference.get().await().getValue<Map<String, RemoteAudio>>()!!
        val audioList = snapshot.values.toList()

        return flow {
            emit(Result.success(audioList))
        }.catch { e -> emit(Result.failure(e)) }
    }

    override suspend fun fetchAudio(audioId: String): Flow<Result<RemoteAudio>> {
        val reference = realtimeDataBase.getReference(FirePath.getAllAudiosPath()).child(audioId)
        return flow {
            val snapshot = reference.get().await()
            val audio = snapshot.getValue<RemoteAudio>() ?: throw Exception("Audio not found")
            emit(Result.success(audio))
        }.catch { e -> emit(Result.failure(e)) }
    }

    override suspend fun isVideoLiked(audioId: String): Flow<Boolean> {
        val reference = realtimeDataBase.getReference(FirePath.getMyLikedAudios()).child(audioId)

        return flow {
            val snapshot = reference.get().await()
            val isLiked = snapshot.exists()
            emit(isLiked)
        }.catch { emit(false) }
    }

    override suspend fun likeOrUnlikeVideo(audioId: String, authorId: String, shouldLike: Boolean) {
        val myLikedVideos = realtimeDataBase
            .getReference(FirePath.getMyLikedAudios())
            .child(audioId)

        val videoRef = realtimeDataBase
            .getReference(FirePath.getAllAudiosPath())
            .child(audioId)
            .child("likes")

        val authorTotalLikesCountRef = realtimeDataBase
            .getReference(FirePath.getUserInfo(authorId))
            .child("totalLikes")


        myLikedVideos.setValue(if (shouldLike) audioId else null)

        var videoLikeCount = videoRef.get().await().getValue<Int>() ?: 0
        if (shouldLike) videoLikeCount++ else videoLikeCount--
        videoRef.setValue(videoLikeCount)

        var totalLikeCount = authorTotalLikesCountRef.get().await().getValue<Int>() ?: 0
        if (shouldLike) totalLikeCount++ else totalLikeCount--
        authorTotalLikesCountRef.setValue(totalLikeCount)
    }

    private fun getRemoteAudioFromLocalVideo(
        videoUrl: String,
        descriptionText: String,
        duration: Long?,
        image: String
    ) =
        RemoteAudio(
            url = videoUrl,
            description = descriptionText,
            image = image,
            duration = duration ?: 0,
            audioId = UUID.randomUUID().toString(),
            dateCreated = System.currentTimeMillis(),
            likes = 0,
            views = 0,
            authorUid = Firebase.auth.uid ?: ""
        )

    override suspend fun uploadAudio(localAudioUri: String): Flow<Result<Uri>> {
        val storageRefer =
            fireStorage.getReference("${FirePath.getAllAudiosPath()}/${Firebase.auth.uid}/${UUID.randomUUID()}")
        return flow {
            val uploadTask = storageRefer.putFile(Uri.fromFile(File(localAudioUri))).await()
            val url = uploadTask.storage.downloadUrl.await()
            emit(Result.success(url))
        }.catch { e ->
            Log.d("joka", e.message.toString())
            emit(Result.failure(e)) }
    }

    override suspend fun saveVideoToFireDB(
        isPrivate: Boolean,
        videoUrl: String,
        descriptionText: String,
        image: String,
        duration: Long?,
        onComplete: (Boolean) -> Unit
    ) {
        try {
            val audioType = if (isPrivate) AudioType.PRIVATE else AudioType.PUBLIC
            val remoteAudio =
                getRemoteAudioFromLocalVideo(videoUrl = videoUrl, descriptionText = descriptionText, image = image, duration = duration)

            if (!isPrivate) {
                makeVideoPublic(remoteAudio)
            }
            saveVideoToMyAccount(audioType, remoteAudio)
            onComplete(true)
        } catch (e: Exception) {
            onComplete(false)
        }
    }

    private suspend fun saveVideoToMyAccount(
        audioType: AudioType,
        remoteAudio: RemoteAudio
    ) {
        realtimeDataBase
            .getReference(FirePath.getUserAudios(Firebase.auth.uid!!, audioType))
            .child(remoteAudio.audioId).setValue(remoteAudio.audioId)
            .await()
    }

    private suspend fun makeVideoPublic(
        remoteAudio: RemoteAudio
    ) {
        realtimeDataBase
            .getReference(FirePath.getAllAudiosPath())
            .child(remoteAudio.audioId).setValue(remoteAudio)
            .await()
    }
}