package com.example.amusegrind.recorder.presentation

import com.example.amusegrind.recorder.domain.entities.LoadProgress

data class RecorderState(val recordingStatus: LoadProgress = LoadProgress.FAILED)
