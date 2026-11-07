package ru.mirea.svinarenkomd.weatherwardrobe.domain.models

data class RecognitionResult(
    val detectedLabel: String,
    val mappedCategory: String,
    val recommendedMinTemp: Int,
    val recommendedMaxTemp: Int,
    val confidence: Float
)
