package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.RecognitionResult
import ru.mirea.svinarenkomd.domain.repository.ClassifierRepository

class RecognizeClothingByPhotoUseCase(
    private val classifierRepository: ClassifierRepository
) {
    fun execute(imagePath: String): RecognitionResult {
        return classifierRepository.recognizeClothing(imagePath)
    }
}
