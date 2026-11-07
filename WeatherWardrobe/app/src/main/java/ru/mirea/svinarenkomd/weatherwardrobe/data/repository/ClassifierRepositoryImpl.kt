package ru.mirea.svinarenkomd.weatherwardrobe.data.repository

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.RecognitionResult
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.ClassifierRepository

class ClassifierRepositoryImpl : ClassifierRepository {

    override fun recognize(imageTag: String): RecognitionResult {
        return when (imageTag.lowercase()) {
            "trench", "плащ", "тренч" -> RecognitionResult(
                detectedLabel = "trench coat",
                mappedCategory = "Верхняя одежда (Плащ / Пальто)",
                recommendedMinTemp = 5,
                recommendedMaxTemp = 16,
                confidence = 0.94f
            )
            "tshirt", "футболка" -> RecognitionResult(
                detectedLabel = "jersey, T-shirt",
                mappedCategory = "Верх (Футболка)",
                recommendedMinTemp = 20,
                recommendedMaxTemp = 35,
                confidence = 0.91f
            )
            else -> RecognitionResult(
                detectedLabel = "sweatshirt",
                mappedCategory = "Верх (Худи / Толстовка)",
                recommendedMinTemp = 10,
                recommendedMaxTemp = 20,
                confidence = 0.92f
            )
        }
    }
}
