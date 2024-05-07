package com.example.amusegrind.network.data

import com.example.amusegrind.network.domain.entities.image.ImageRequestBody
import com.example.amusegrind.network.domain.entities.image.ImageResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ImageApiService {
    @POST("text2img")
    suspend fun postImageRequest(@Body imageRequestBody: ImageRequestBody): Result<ImageResponse>
}