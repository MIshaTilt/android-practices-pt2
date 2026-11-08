package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.domain.repository.ClothingRepository

class SaveClothingToWardrobeUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(item: ClothingItem): Boolean {
        return clothingRepository.saveToWardrobe(item)
    }
}
