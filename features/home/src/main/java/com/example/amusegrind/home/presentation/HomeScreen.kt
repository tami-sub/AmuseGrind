package com.example.amusegrind.home.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel: VideoPlayerViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value
//    val author by viewModel.author.collectAsState()
//    val likeCount by viewModel.likeCount.collectAsState()
//    val isVideoLiked by viewModel.isVideoLiked.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
//        TopAppBar(
//            title = { Text(text = author?.username ?: "@...") },
//            navigationIcon = {
//                IconButton(onClick = { /* Handle back press */ }) {
//                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                }
//            }
//        )

        state.remoteAudioList?.let {
            it.forEach { audio ->
                Log.d("joka", audio.url.toString())
            }
            VideoPlayer(modifier = Modifier.weight(1f), viewModel, it)
        }

//        Box(
//            modifier = Modifier
//                .width(120.dp)
//                .height(120.dp)
//                .background(Color.Red)
//                .clickable { viewModel.fetchVideos() }
//                .align(Alignment.CenterHorizontally)
//        )


//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "Likes: ${NumbersUtils.formatCount(likeCount)}")
//            IconButton(
//                onClick = { viewModel.likeOrUnlikeVideo(currentRemoteAudio) },
//                icon = { Icon(if (isVideoLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = "Like") }
//            )
//        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier,
    viewModel: VideoPlayerViewModel,
    remoteAudio: List<RemoteAudio>
) {
    val state = rememberSwipeableState(0)
    val size = Modifier
        .fillMaxSize()
        .swipeable(
            state = state,
            anchors = mapOf(0f to 0, 1f to 1),
            thresholds = { _, _ -> FractionalThreshold(0.3f) },
            orientation = Orientation.Vertical
        )

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = ExoPlayer.Builder(context).build().also {
                    it.setMediaItem(MediaItem.fromUri(remoteAudio[0].url))
                    it.prepare()
                    it.playWhenReady = true
                }
            }
        },
        modifier = size
    )
}

