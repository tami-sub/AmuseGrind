package com.example.amusegrind.recorder.domain.usecase

import com.example.amusegrind.network.data.ImageApiService
import com.example.amusegrind.network.domain.entities.image.ImageRequestBody
import javax.inject.Inject

class ImageGeneratorUseCase @Inject constructor(private val imageApiService: ImageApiService) {
    suspend operator fun invoke(prompt: String) =
        imageApiService.postImageRequest(ImageRequestBody(prompt = prompt))
}