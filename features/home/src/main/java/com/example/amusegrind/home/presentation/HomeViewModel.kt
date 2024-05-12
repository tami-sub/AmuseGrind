package com.example.amusegrind.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.network.data.AudiosRepo
import com.example.amusegrind.network.data.UserRepo
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val audiosRepo: AudiosRepo,
    private val userRepo: UserRepo,
) : ViewModel() {

    private var _state = MutableStateFlow(HomeState(null, null))
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        fetchVideos()
    }

    fun getAuthorImageUrl(authorId: String) {
        viewModelScope.launch {
            userRepo.getUserProfile(authorId).first().onSuccess { user ->
                _state.update { it.copy(authorImageUrl = user?.profilePictureUrl) }
            }
        }
    }

    fun fetchVideos() {
        viewModelScope.launch {
            try {
                audiosRepo.fetchRandomAudios().first().onSuccess { list ->
                    _state.update { it.copy(remoteAudioList = list) }
                }.onFailure {
                    Log.d("joka", "Failed fetch videos")
                }
            }
            catch (e: Exception){}
        }
    }

    fun checkIfAudioLiked(remoteAudio: RemoteAudio) {
        viewModelScope.launch {
            val isLiked = audiosRepo.isVideoLiked(remoteAudio.audioId).first()
            _state.update { it.copy(isLiked = isLiked) }
        }
    }

    fun likeOrUnlikeAudio(remoteAudio: RemoteAudio, audioIndex: Int) {
        viewModelScope.launch {
            val changedLike = !state.value.isLiked
            _state.update {
                it.remoteAudioList?.let { audios ->
                    val likeCount = audios[audioIndex].likes.toInt()
                    val newLikeCount = if (changedLike) likeCount + 1 else likeCount - 1
                    it.remoteAudioList.get(audioIndex).likes = newLikeCount.toLong()
                }
                it.copy(
                    isLiked = changedLike,
                )
            }

            audiosRepo.likeOrUnlikeVideo(
                audioId = remoteAudio.audioId,
                authorId = remoteAudio.authorUid,
                shouldLike = changedLike
            )
        }
    }
}
