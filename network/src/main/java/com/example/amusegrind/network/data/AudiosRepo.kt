package com.example.amusegrind.network.data

import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
import kotlinx.coroutines.flow.Flow

interface AudiosRepo {

    suspend fun fetchRandomAudios(): Flow<Result<List<RemoteAudio>>>

    suspend fun fetchAudio(audioId: String): Flow<Result<RemoteAudio>>

    suspend fun isVideoLiked(audioId: String): Flow<Boolean>

    suspend fun likeOrUnlikeVideo(audioId: String, authorId: String, shouldLike: Boolean)

    suspend fun saveVideoToFireDB(
        isPrivate: Boolean,
        videoUrl: String,
        descriptionText: String,
        duration: Long?,
        onComplete: (Boolean) -> Unit
    )
}