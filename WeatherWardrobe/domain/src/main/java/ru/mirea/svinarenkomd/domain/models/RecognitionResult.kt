package ru.mirea.svinarenkomd.domain.models

data class RecognitionResult(
    val recognizedLabel: String,
    val confidence: Float,
    val suggestedCategory: String,
    val suggestedMinTemp: Int,
    val suggestedMaxTemp: Int
)
