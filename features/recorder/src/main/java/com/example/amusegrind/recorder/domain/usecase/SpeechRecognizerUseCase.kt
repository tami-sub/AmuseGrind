package com.example.amusegrind.recorder.domain.usecase

import com.example.amusegrind.network.data.YandexSpeechKitService
import okhttp3.RequestBody
import javax.inject.Inject

class SpeechRecognizerUseCase @Inject constructor(private val retrofitClient: YandexSpeechKitService) {
    suspend operator fun invoke(requestBody: RequestBody) =
        retrofitClient.recognizeSpeech(language = "en-US", audio = requestBody)
}