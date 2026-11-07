package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.ClothingRepository

class SaveClothingToWardrobeUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(item: ClothingItem): Boolean {
        if (item.name.isBlank()) {
            return false
        }
        return clothingRepository.saveToWardrobe(item)
    }
}
