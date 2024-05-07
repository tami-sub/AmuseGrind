package com.example.amusegrind.network.domain.entities.image

import kotlin.random.Random

data class ImageRequestBody(
    val prompt: String,
    val negative_prompt: String = "Stop banner",
    val scheduler: String = "EulerAncestralDiscreteScheduler",
    val image_height: Int = 512,
    val image_width: Int = 512,
    val num_images: Int = 1,
    val guidance_scale: Int = 7,
    val steps: Int = 30,
    val seed: Int = Random.nextInt(1, 50)
)