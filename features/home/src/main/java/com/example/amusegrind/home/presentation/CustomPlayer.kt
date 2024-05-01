package com.example.amusegrind.home.presentation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.amusegrind.network.domain.entities.audio.RemoteAudio

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomPlayer(
    modifier: Modifier = Modifier,
    remoteAudioList: List<RemoteAudio>,
    audioIndex: MutableIntState,
    getAuthorImageUrl: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(remoteAudioList[audioIndex.value].url))
            prepare()
            playWhenReady = true
        }
    }
    getAuthorImageUrl()

    val screenHeight = LocalConfiguration.current.screenHeightDp.toFloat()
    val anchors = (0 until remoteAudioList.size).associate { index ->
        (-index * screenHeight) to index.toFloat() // Both keys and values are Floats
    }
    val swipeableState = rememberSwipeableState(initialValue = 0f)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                player.playWhenReady = false
            } else if (event == Lifecycle.Event.ON_START) {
                player.playWhenReady = true
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }

    Box(
        modifier = modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Vertical
            )
    ) {
        if (swipeableState.currentValue != audioIndex.value.toFloat()) {
            audioIndex.value = swipeableState.currentValue.toInt()
            player.setMediaItem(MediaItem.fromUri(remoteAudioList[audioIndex.value].url))
            player.prepare()
            player.playWhenReady = true
        }

        AndroidView(
            factory = { PlayerView(it).apply { this.player = player } },
            modifier = Modifier.fillMaxSize()
        )
    }
}