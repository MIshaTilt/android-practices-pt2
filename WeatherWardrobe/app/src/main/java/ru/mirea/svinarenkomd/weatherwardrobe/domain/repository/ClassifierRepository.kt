package ru.mirea.svinarenkomd.weatherwardrobe.domain.repository

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.RecognitionResult

interface ClassifierRepository {
    fun recognize(imageTag: String): RecognitionResult
}
