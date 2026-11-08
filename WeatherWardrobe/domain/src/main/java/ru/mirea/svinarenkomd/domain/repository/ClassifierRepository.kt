package ru.mirea.svinarenkomd.domain.repository

import ru.mirea.svinarenkomd.domain.models.RecognitionResult

interface ClassifierRepository {
    fun recognizeClothing(imagePath: String): RecognitionResult
}
