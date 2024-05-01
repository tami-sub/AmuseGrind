package com.example.amusegrind.settings.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.amusegrind.core.ui.AuthButton
import com.example.amusegrind.settings.R

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()
    viewModel.getAccountInfo()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .align(Alignment.CenterHorizontally),
            painter = rememberAsyncImagePainter(state.value.user.profilePictureUrl?.toUri()),
            contentDescription = null
        )
        Text(text = state.value.user.username ?: "", Modifier.align(Alignment.CenterHorizontally))
        Text(
            text = "Your total likes: ${state.value.user.totalLikes}",
            Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthButton(
            text = stringResource(id = R.string.sign_out),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            viewModel.signOut()
        }
    }
}