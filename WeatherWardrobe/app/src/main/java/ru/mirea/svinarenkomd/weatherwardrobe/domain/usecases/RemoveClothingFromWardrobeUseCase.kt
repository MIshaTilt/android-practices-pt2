package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.ClothingRepository

class RemoveClothingFromWardrobeUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(clothingId: Int): Boolean {
        return clothingRepository.removeFromWardrobe(clothingId)
    }
}
