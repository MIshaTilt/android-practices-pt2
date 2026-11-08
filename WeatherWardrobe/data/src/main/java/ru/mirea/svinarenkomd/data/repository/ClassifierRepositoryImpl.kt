package ru.mirea.svinarenkomd.data.repository

import ru.mirea.svinarenkomd.domain.models.RecognitionResult
import ru.mirea.svinarenkomd.domain.repository.ClassifierRepository

class ClassifierRepositoryImpl : ClassifierRepository {

    override fun recognizeClothing(imagePath: String): RecognitionResult {
        val normalized = imagePath.lowercase()
        return when {
            normalized.contains("coat") || normalized.contains("jacket") || normalized.contains("куртк") -> {
                RecognitionResult(
                    recognizedLabel = "Мембранная ветровка Urban",
                    confidence = 0.94f,
                    suggestedCategory = "Верхняя одежда (Плащ / Ветровка)",
                    suggestedMinTemp = 8,
                    suggestedMaxTemp = 18
                )
            }
            normalized.contains("parka") || normalized.contains("fur") || normalized.contains("пуховик") -> {
                RecognitionResult(
                    recognizedLabel = "Зимний пуховик Arctic",
                    confidence = 0.96f,
                    suggestedCategory = "Верхняя одежда (Зимняя куртка)",
                    suggestedMinTemp = -25,
                    suggestedMaxTemp = 0
                )
            }
            normalized.contains("sneaker") || normalized.contains("shoe") || normalized.contains("кед") -> {
                RecognitionResult(
                    recognizedLabel = "Кожаные кеды Street",
                    confidence = 0.91f,
                    suggestedCategory = "Обувь",
                    suggestedMinTemp = 5,
                    suggestedMaxTemp = 25
                )
            }
            else -> {
                RecognitionResult(
                    recognizedLabel = "Оверсайз худи с начесом",
                    confidence = 0.92f,
                    suggestedCategory = "Верх (Худи и свитшоты)",
                    suggestedMinTemp = 10,
                    suggestedMaxTemp = 20
                )
            }
        }
    }
}
