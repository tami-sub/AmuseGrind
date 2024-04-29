package com.example.amusegrind.home.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
import kotlin.math.roundToInt

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
            VideoPlayer(modifier = Modifier, viewModel, it)
        }






//        state.remoteAudioList?.let {
//            it.forEach { audio ->
//                Log.d("joka", audio.url.toString())
//            }
//            VideoPlayer(modifier = Modifier.weight(1f), viewModel, it)
//        }


        Box(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .background(Color.Red)
                .clickable { viewModel.fetchVideos() }
                .align(Alignment.CenterHorizontally)
        )


        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Likes: ${0}")
            IconButton(
                onClick = { },
            ){ Icon(Icons.Filled.Favorite, contentDescription = null) }
        }
    }
}

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    viewModel: VideoPlayerViewModel,
    remoteAudio: List<RemoteAudio>
) {
    val audioIndex = remember { mutableStateOf(0) }
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(remoteAudio[audioIndex.value].url))
            prepare()
            playWhenReady = true
        }
    }

    // Calculate swipe thresholds
    val screenHeight = LocalConfiguration.current.screenHeightDp/6  // Replace with actual screen height or any suitable value
    val maxIndex = remoteAudio.size - 1
    val scrollState = rememberDraggableState { delta ->
        val newIndex = ((audioIndex.value * screenHeight - delta) / screenHeight).coerceIn(0F, maxIndex.toFloat()).roundToInt()
        if (newIndex != audioIndex.value) {
            audioIndex.value = newIndex
            player.setMediaItem(MediaItem.fromUri(remoteAudio[audioIndex.value].url))
            player.prepare()
            player.playWhenReady = true
        }
        delta
    }

    val dragModifier = Modifier
        .fillMaxSize()
        .draggable(
            state = scrollState,
            orientation = Orientation.Vertical,
            onDragStopped = { /* Handle logic if needed when drag stops */ }
        )

    AndroidView(
        factory = { PlayerView(it).apply { this.player = player } },
        modifier = modifier.then(dragModifier)
    )
}


