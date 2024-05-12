package com.example.amusegrind.network.data

import com.example.amusegrind.core.utils.Constants
import com.example.amusegrind.network.domain.entities.audio.SpeechRecognitionResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface YandexSpeechKitService {
    @Headers("Authorization: Api-Key api_key")
    @POST(Constants.yandexSpeechKitFullUrl)
    suspend fun recognizeSpeech(
        @Query("lang") language: String,
        @Body audio: RequestBody
    ): Result<SpeechRecognitionResponse>
}