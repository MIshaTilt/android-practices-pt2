package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.RecognitionResult
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.ClassifierRepository

class RecognizeClothingByPhotoUseCase(
    private val classifierRepository: ClassifierRepository
) {
    fun execute(imageTag: String): RecognitionResult {
        return classifierRepository.recognize(imageTag)
    }
}
