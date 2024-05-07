package com.example.amusegrind.network.domain.entities.image

data class Metadata(
    val prompt: String,
    val negative_prompt: String,
    val scheduler: String,
    val image_height: Int,
    val image_width: Int,
    val num_images: Int,
    val guidance_scale: Double,
    val steps: Int,
    val seed: Int
)