package com.example.amusegrind.home.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.network.data.VideosRepo
import com.example.amusegrind.network.domain.entities.User
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val navigator: Navigator,
    private val videosRepo: VideosRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state = MutableStateFlow(PlayerState(null))
    val state: StateFlow<PlayerState> = _state.asStateFlow()

//    private var isFetching = false

    init {
        fetchVideos()
    }
    fun fetchVideos() {

//        if (!isFetching) {
//            isFetching = true
        viewModelScope.launch {
            videosRepo.fetchRandomAudios().first().onSuccess { list ->
                _state.update { it.copy(remoteAudioList = list) }
                Log.d("joka", state.value.remoteAudioList.toString())
//                        isFetching = false
            }
                .onFailure {
                    Log.d("joka", "Failed fetch videos")
                }
//            videosRepo.fetchRandomAudios().collect { result ->
//                result.onSuccess { list ->
//                    _state.update { it.copy(remoteAudioList = list) }
//                    Log.d("joka", state.value.remoteAudioList.toString())
////                        isFetching = false
//                }
//                    .onFailure {
//                        Log.d("joka", "Failed fetch videos")
//                    }
////                }


//                videosRepo.fetchRandomAudios().first().onSuccess {
//                    _state.update { it.copy(remoteAudioList = it.remoteAudioList) }
//                    Log.d("boka", state.value.remoteAudioList.toString())
//                    isFetching = false
//                }.onFailure {
//                    Log.d("joka", "Failed fetch videos")
//                }
            }
        }
//    }

//    private val _author = MutableStateFlow<User?>(null)
//    val author: StateFlow<User?> = _author.asStateFlow()
//
//    private val _likeCount = MutableStateFlow<Int>(0)
//    val likeCount: StateFlow<Int> = _likeCount.asStateFlow()
//
//    private val _isVideoLiked = MutableStateFlow<Boolean>(false)
//    val isVideoLiked: StateFlow<Boolean> = _isVideoLiked.asStateFlow()

//    fun init(remoteAudioId: String) {
//        viewModelScope.launch {
//            val remoteAudio = videosRepo(remoteAudioId)
//            _author.value = userRepo.getUserProfile(remoteAudio.authorUid).tryData()
//            _likeCount.value = remoteAudio.likes.toInt()
//            checkIfVideoLiked(remoteAudio.audioId)
//        }
//    }

//    private suspend fun checkIfVideoLiked(audioId: String) {
//        val isLiked = videosRepo.isVideoLiked(audioId)
//        _isVideoLiked.value = isLiked.succeeded && isLiked.forceData()
//    }

//    fun likeOrUnlikeVideo(remoteAudio: RemoteAudio) {
//        viewModelScope.launch {
//            val shouldLike = !_isVideoLiked.value
//            _isVideoLiked.value = shouldLike
//            _likeCount.value = if (shouldLike) _likeCount.value + 1 else _likeCount.value - 1
//
//            videosRepo.likeOrUnlikeVideo(
//                audioId = remoteAudio.audioId,
//                authorId = remoteAudio.authorUid,
//                shouldLike = shouldLike
//            )
//        }
//    }
}
