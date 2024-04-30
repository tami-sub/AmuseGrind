package com.example.amusegrind.home.presentation

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        state.remoteAudioList?.let {
            VideoPlayer(modifier = Modifier, viewModel, it)
        }

        Column(
            modifier = Modifier
                .padding(bottom = 100.dp, end = 16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(text = "Likes: ${0}", color = Color.White)
            IconButton(
                onClick = { },
            ) { Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.White) }
        }
    }
}

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    remoteAudio: List<RemoteAudio>
) {
    val audioIndex = remember { mutableStateOf(0) }
    Log.d("joka", audioIndex.toString())
    Log.d("joka", remoteAudio.toString())
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(remoteAudio[audioIndex.value].url))
            prepare()
            playWhenReady = true
        }
    }

    // Calculate swipe thresholds
    val screenHeight =
        LocalConfiguration.current.screenHeightDp / 2  // Replace with actual screen height or any suitable value
    val maxIndex = remoteAudio.size - 1
    val scrollState = rememberDraggableState { delta ->
        val newIndex = ((audioIndex.value * screenHeight - delta) / screenHeight).coerceIn(
            0F, maxIndex.toFloat()
        ).roundToInt()
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

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                player.playWhenReady = false
                player.release()
            } else if (event == Lifecycle.Event.ON_START) {
                player.playWhenReady = true
                player.prepare()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }

    AndroidView(
        factory = { PlayerView(it).apply { this.player = player } },
        modifier = modifier.then(dragModifier)
    )
}


