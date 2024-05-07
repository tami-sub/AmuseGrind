package com.example.amusegrind.network.domain.entities.image

data class ImageResponse(
    val images: List<String>,
    val metadata: Metadata
)