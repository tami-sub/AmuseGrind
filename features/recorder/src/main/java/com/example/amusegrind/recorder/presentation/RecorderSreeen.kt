package com.example.amusegrind.recorder.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amusegrind.recorder.R

@Composable
fun RecorderScreen() {
    val viewModel: RecorderViewmodel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 150.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = { viewModel.startRecording()}) {
            Text(text = stringResource(R.string.start_recording))
        }
        Spacer(modifier = Modifier.height(80.dp))
        Button(onClick = { viewModel.stopRecording() }) {
            Text(text = stringResource(R.string.stop_recording))
        }
    }
}
