package com.example.amusegrind.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    val audioIndex = remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        state.remoteAudioList?.let { audiosList ->
            CustomPlayer(
                modifier = Modifier,
                remoteAudioList = audiosList,
                audioIndex = audioIndex
            ) {
                viewModel.getAuthorImageUrl(audiosList[audioIndex.intValue].authorUid)
            }
            viewModel.checkIfAudioLiked(audiosList[audioIndex.intValue])
        }

        Column(
            modifier = Modifier
                .padding(bottom = 150.dp, end = 16.dp)
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable {
                        // TODO NavigateToChat
                    },
                painter = rememberAsyncImagePainter(state.authorImageUrl?.toUri()),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(20.dp))
//            Text(text = "Likes: ${0}", color = Color.White)
            Text(
                text = "Likes: ${state.remoteAudioList?.get(audioIndex.intValue)?.likes}",
                color = Color.White
            )
            IconButton(
                onClick = {
                    state.remoteAudioList?.let {
                        viewModel.likeOrUnlikeAudio(it[audioIndex.intValue], audioIndex.intValue)
                    }
                },
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = if (state.isLiked) Color.Red else Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}
