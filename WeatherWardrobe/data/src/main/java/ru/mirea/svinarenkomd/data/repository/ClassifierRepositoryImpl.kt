package ru.mirea.svinarenkomd.data.repository

import ru.mirea.svinarenkomd.domain.models.RecognitionResult
import ru.mirea.svinarenkomd.domain.repository.ClassifierRepository

class ClassifierRepositoryImpl : ClassifierRepository {

    override fun recognizeClothing(imagePath: String): RecognitionResult {
        val normalized = imagePath.lowercase()
        return when {
            normalized.contains("coat") || normalized.contains("jacket") || normalized.contains("куртк") -> {
                RecognitionResult(
                    recognizedLabel = "trench coat (Куртка / Плащ)",
                    confidence = 0.94f,
                    suggestedCategory = "Верхняя одежда (Плащ / Пальто)",
                    suggestedMinTemp = 5,
                    suggestedMaxTemp = 16
                )
            }
            normalized.contains("tee") || normalized.contains("shirt") || normalized.contains("футболк") -> {
                RecognitionResult(
                    recognizedLabel = "jersey, T-shirt (Футболка)",
                    confidence = 0.91f,
                    suggestedCategory = "Верх (Футболка)",
                    suggestedMinTemp = 20,
                    suggestedMaxTemp = 35
                )
            }
            normalized.contains("jean") || normalized.contains("джинс") -> {
                RecognitionResult(
                    recognizedLabel = "jean, blue jean (Джинсы)",
                    confidence = 0.95f,
                    suggestedCategory = "Низ (Джинсы / Брюки)",
                    suggestedMinTemp = -5,
                    suggestedMaxTemp = 22
                )
            }
            else -> {
                RecognitionResult(
                    recognizedLabel = "sweatshirt (Худи / Толстовка)",
                    confidence = 0.92f,
                    suggestedCategory = "Верх (Худи / Толстовка)",
                    suggestedMinTemp = 10,
                    suggestedMaxTemp = 20
                )
            }
        }
    }
}
